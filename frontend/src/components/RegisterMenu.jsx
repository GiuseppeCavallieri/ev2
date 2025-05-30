import React from 'react';
import Card from 'react-bootstrap/Card';
import { Link } from 'react-router-dom';
import { useState } from 'react';
import { Form } from 'react-bootstrap';
import userService from '../services/userService';
import { useNavigate } from 'react-router-dom';


const RegisterMenu = () =>{
    // Se usa para navegar entre rutas
    const navigate = useNavigate();
    
    // Se usa para guardar los datos
    const [formData, setFormData] = useState({
        name: '',
        password: '',
        email: '',
        fechaNacimiento: '', // yyyy-MM-dd
        superuser: false,
    });

    const [error, setError] = useState('');

    // Se usa para actualizar los datos del formulario
    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    }

    // Conexión con el backend
    const handleSubmit = async (e) => {
        e.preventDefault(); // Evita que la página se recargue al enviar el formulario
        try {
          const response = await userService.register(formData.name, formData.password, formData.email, formData.fechaNacimiento, formData.superuser);
    
          // Si la respuesta es exitosa, redirige al menú
          if (response.status === 200) {
            navigate('/');
          }
    
        // Si la respuesta no es exitosa, muestra un mensaje de error
        } catch (error) {
          if (error.response && error.response.status === 401) {
            setError('Credenciales incorrectas');
          } else {
            setError(error.response.data);
          }
        }
    }

    return (

        
        <div 
        className="d-flex justify-content-center align-items-center" 
        style={{ 
            height: '100vh', 
            overflowY: 'auto', 
            padding: '6rem'
        }}
        >
            <Card className="shadow-lg" style={{ width: '25rem', padding: '2rem' }}>
                <Card.Body>
                    <h1 className="text-center mb-4">Registro</h1>

                    {error && <p className="text-danger text-center">{error}</p>}

                    <form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Nombre</Form.Label>
                            <Form.Control
                                    type="text" 
                                    name="name" 
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
                                    value={formData.password} 
                                    onChange={handleChange} 
                                    required 
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                    type="email" 
                                    name="email" 
                                    value={formData.email} 
                                    onChange={handleChange} 
                                    required 
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Fecha de Nacimiento</Form.Label>
                            <Form.Control
                                    type="date" 
                                    name="fechaNacimiento" 
                                    value={formData.fechaNacimiento} 
                                    onChange={handleChange} 
                                    required 
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Check 
                                type="checkbox" 
                                label="Superusuario" 
                                name="superuser" 
                                checked={formData.superuser} 
                                onChange={(e) => setFormData({ ...formData, superuser: e.target.checked })} 
                            />
                        </Form.Group>

                        <button type="submit" className="btn btn-primary w-100">
                            Registrarse
                        </button>
                    </form>

                    <p className="text-center mt-3">
                        <Link to="/" style={{ textDecoration: "underline", color: "blue" }}>
                            Volver a login
                        </Link>
                    </p>
                </Card.Body>
            </Card>
        </div>
    )
}

export default RegisterMenu;