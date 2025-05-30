import React from 'react';
import { Card, Form, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import userService from '../services/userService';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';



const Login = () => {
    // Se usa para navegar entre rutas
    const navigate = useNavigate();

    // Se usa para guardar los datos
    const [formData, setFormData] = useState({
      name: '',
      password: '',
    });

    // Se usa para actualizar los datos del formulario
    const handleChange = (e) => {
      setFormData({
          ...formData,
          [e.target.name]: e.target.value,
      });
    };

    const [error, setError] = useState(''); 

    // Conexión con el backend
    const handleSubmit = async (e) => {
        e.preventDefault(); // Evita que la página se recargue al enviar el formulario
        try {
          const response = await userService.login(formData.name, formData.password);
    
          // Si la respuesta es exitosa, guarda el token en el localStorage y redirige al menú
          if (response.status === 200) {
            const user = { name: formData.name };
            localStorage.setItem('user', JSON.stringify(user));
            navigate('/Menu');
          }

        // Si la respuesta no es exitosa, muestra un mensaje de error
        } catch (error) {
          if (error.response && error.response.status === 401) {
            setError('Credenciales incorrectas');
          } else {
            setError('Hubo un error al iniciar sesión. Intenta nuevamente.');
          }
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
            <Card className="shadow-lg" style={{ width: '25rem', padding: '2rem' }}>
                <Card.Body>
                    <h1 className="text-center mb-4">Login</h1>

                    {error && <p className="text-danger text-center">{error}</p>}

                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Nombre de usuario</Form.Label>
                            <Form.Control
                                type="text"
                                name="name"
                                placeholder="Ingrese su nombre"
                                value={formData.name}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Contraseña</Form.Label>
                            <Form.Control
                                type="password"
                                name="password"
                                placeholder="Ingrese su contraseña"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>

                        <Button variant="primary" type="submit" className="w-100">
                            Iniciar sesión
                        </Button>
                    </Form>

                    <p className="text-center mt-3">
                        ¿No tienes cuenta?  
                        <Link to="/register" style={{ textDecoration: "underline", color: "blue" }}>
                            Regístrate aquí
                        </Link>
                    </p>
                </Card.Body>
            </Card>
        </div>
    )
}

export default Login;