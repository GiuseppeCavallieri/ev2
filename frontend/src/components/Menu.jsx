import React, { useState, useEffect, use } from 'react';
import { Card, Form, Tab, Tabs, Button, Modal, ListGroup, Table } from 'react-bootstrap';
import { startOfWeek, addDays, format, subWeeks, addWeeks, set } from 'date-fns';
import { es } from 'date-fns/locale';

import userService from '../services/userService';
import rateService from '../services/rateService';
import discountService from '../services/discountService';
import kartService from '../services/kartService';
import reservationService from '../services/reservationService';


const Menu = () => {
  const [activeTab, setActiveTab] = useState('horario'); // la pestaña activa, horario se abre por defecto

  // Const usadas en pestaña horario
  const [showDayModal, setShowDayModal] = useState(false); // se muestran las horas de un día
  const [selectedDay, setSelectedDay] = useState(null); // día seleccionado para mostrar horas
  const [selectedDate, setSelectedDate] = useState(null); // fecha seleccionada para la reserva
  const [currentDate, setCurrentDate] = useState(new Date()); // fecha actual, se usa para mostrar la semana correspondiente
  const [availableKarts, setAvailableKarts] = useState(null); // guarda los karts disponibles para el día seleccionado
  const [showReservaModal, setShowReservaModal] = useState(false);
  const [selectedHour, setSelectedHour] = useState(null); // hora seleccionada para la reserva
  const [formReservation, setFormReservation] = useState({
    clientId: '',
    companionsId: [],
    rateCode: '',
    hourChoosen: '',
    dateChoosen: ''
  }); // guarda los datos de la reserva
  const [reservationErrors, setReservationErrors] = useState({}); // errores del formulario de reservas
  const [reservedHours, setReservedHours] = useState([]); // guarda las horas reservadas para el día seleccionado

  // Const usada para mostrar la pestaña de admin
  const [isSuperUser, setIsSuperUser] = useState(false);
  //--- Para mostrar tarifas y descuentos en tablas
  const [rates, setRates] = useState([]); // guarda los tarifas de la base de datos
  const [discounts, setDiscounts] = useState([]); // guarda los descuentos de la base de datos
  const [discountsFreq, setDiscountsFreq] = useState([]); // guarda los descuentos de la base de datos
  const [karts, setKarts] = useState([]); // guarda los karts de la base de datos
  const [specialRates, setSpecialRates] = useState([]); // guarda las tarifas especiales de la base de datos
  //--- Para editar tarifas y descuentos
  const [showRateModal, setShowRateModal] = useState(false); // se muestra el modal para editar tarifas
  const [showDiscountModal, setShowDiscountModal] = useState(false); // se muestra el modal para editar descuentos
  const [showDiscountFreqModal, setShowDiscountFreqModal] = useState(false); // se muestra el modal para editar descuentos frecuentes
  const [showSpecialRateModal, setShowSpecialRateModal] = useState(false); // se muestra el modal para editar tarifas especiales
  const [rateFormValues, setRateFormValues] = useState({ code: '', price: '', duration: '', description: '' }); // valores del formulario de tarifas
  const [discountFormValues, setDiscountFormValues] = useState({ code: '', limInf: '', limSup: '', discount: '', description: '' }); // valores del formulario de descuentos
  const [discountfreqFormValues, setDiscountFreqFormValues] = useState({ code: '', limInf: '', limSup: '', discount: '', description: '' }); // valores del formulario de descuentos frecuentes
  const [specialRateFormValues, setSpecialRateFormValues] = useState({ month: '', day: '', discount: '', description: '' }); // valores del formulario de tarifas especiales
  const [rateErrors, setRateErrors] = useState({}); // errores del formulario de tarifas
  const [discountErrors, setDiscountErrors] = useState({}); // errores del formulario de descuentos
  const [discountfreqErrors, setDiscountFreqErrors] = useState({}); // errores del formulario de descuentos frecuentes
  const [specialRateErrors, setSpecialRateErrors] = useState({}); // errores del formulario de tarifas especiales
  //--- Para eliminar tarifas y descuentos
  const [showDeleteRateModal, setShowDeleteRateModal] = useState(false); // se muestra el modal para eliminar tarifas
  const [showDeleteDiscountModal, setShowDeleteDiscountModal] = useState(false); // se muestra el modal para eliminar descuentos
  const [showDeleteDiscountFreqModal, setShowDeleteDiscountFreqModal] = useState(false); // se muestra el modal para eliminar descuentos frecuentes
  const [showDeleteSpecialRateModal, setShowDeleteSpecialRateModal] = useState(false); // se muestra el modal para eliminar tarifas especiales
  const [showDeleteKartModal, setShowDeleteKartModal] = useState(false); // se muestra el modal para eliminar karts
  const [rateToDelete, setRateToDelete] = useState(null); // tarifa a eliminar
  const [discountToDelete, setDiscountToDelete] = useState(null); // descuento a eliminar
  const [discountFreqToDelete, setDiscountFreqToDelete] = useState(null); // descuento frecuente a eliminar
  const [kartToDelete, setKartToDelete] = useState(null); // kart a eliminar
  const [specialRateToDelete, setSpecialRateToDelete] = useState(null); // tarifa especial a eliminar
  const [rateToDeleteError, setRateToDeleteError] = useState({}); // errores del formulario de tarifas a eliminar
  const [discountToDeleteError, setDiscountToDeleteError] = useState({}); // errores del formulario de descuentos a eliminar
  const [discountFreqToDeleteError, setDiscountFreqToDeleteError] = useState({}); // errores del formulario de descuentos frecuentes a eliminar
  const [kartToDeleteError, setKartToDeleteError] = useState({}); // errores del formulario de karts a eliminar
  const [specialRateToDeleteError, setSpecialRateToDeleteError] = useState({}); // errores del formulario de tarifas especiales a eliminar
  //--- Para modificar karts
  const [showKartModal, setShowKartModal] = useState(false); // se muestra el modal para editar karts
  const [kartFormValues, setKartFormValues] = useState({ name: '', mantentionDay: '' }); // valores del formulario de karts
  const [showKartError, setShowKartError] = useState({}); // errores del formulario de karts
  //--- Para mostrar los reportes
  const [fechaInicio, setFechaInicio] = useState('');
  const [fechaFin, setFechaFin] = useState('');
  const [tipoTabla, setTipoTabla] = useState('');
  const [incomeData, setIncomeData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const monthOrder = [
    'JANUARY', 'FEBRUARY', 'MARCH', 'APRIL', 'MAY', 'JUNE',
    'JULY', 'AUGUST', 'SEPTEMBER', 'OCTOBER', 'NOVEMBER', 'DECEMBER'
  ];
  const horas = ['10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00'];
  const rateDescriptions = {
    OP10: "10 vueltas o max 10 min",
    OP15: "15 vueltas o max 15 min",
    OP20: "20 vueltas o max 20 min",
  };
  const groupDescriptions = {
    d1: "1 a 2 personas",
    d3: "3 a 5 personas",
    d6: "6 a 10 personas",
    d11: "11 a 15 personas",
  };
  const orderRateCodes = (codes) => {
    return codes.sort((a, b) => {
      const numA = parseInt(a.replace(/\D/g, ""));
      const numB = parseInt(b.replace(/\D/g, ""));
      return numA - numB;
    });
  };
  
  // Funciones para navegar entre semanas
  const goToPreviousWeek = () => {
    setCurrentDate((prev) => subWeeks(prev, 1));
  };
  const goToNextWeek = () => {
    setCurrentDate((prev) => addWeeks(prev, 1));
  };
  const startOfCurrentWeek = startOfWeek(currentDate, { weekStartsOn: 1 }); // Selecciona lunes como primer día de la semana
  // Genera un array con las fechas de la semana actual
  const weekDates = Array.from({ length: 7 }).map((_, i) => // Array de 7 días
    addDays(startOfCurrentWeek, i) // Calcula la fecha de cada día
  );

  // fetch de descuentos frecuentes
  useEffect(() => {
    const fetchDiscountsFreq = async () => {
      try {
        const response = await discountService.getAllDiscountsFreq();
        setDiscountsFreq(response.data);
      } catch (error) {
        console.error("Error al obtener los descuentos frecuentes:", error);
      }
    };
    fetchDiscountsFreq();
  }, []);

  // fetch de los descuentos
  useEffect(() => {
    const fetchDiscounts = async () => {
      try {
        const response = await discountService.getAllDiscounts();
        setDiscounts(response.data);
      } catch (error) {
        console.error("Error al obtener los descuentos:", error);
      }
    };
    fetchDiscounts();
  }, []);

  // fetch de los rates
  useEffect(() => {
    const fetchRates = async () => {
      try {
        const response = await rateService.getAllRates();
        setRates(response.data);
      } catch (error) {
        console.error("Error al obtener las tarifas:", error);
      }
    };
    fetchRates();
  }, []);

  // actualiza la pestaña activa
  useEffect(() => {
    const savedTab = localStorage.getItem('activeTab');
    if (savedTab) {
      setActiveTab(savedTab);
    }
  }, []);

  // fetch de los karts
  useEffect(() => {
    const fetchKarts = async () => {
    try {
      const response = await kartService.getAllKarts();
      setKarts(response.data);
      } catch (error) {
      console.error("Error al obtener los karts:", error);
      }
    };

    fetchKarts();
  },[]);

  // fetch de las tarifas especiales
  useEffect(() => {
    const fetchSpecialRates = async () => {
      try {
        const response = await rateService.getAllSpecialRates();
        setSpecialRates(response.data);
      } catch (error) {
        console.error("Error al obtener las tarifas especiales:", error);
      }
    }
    fetchSpecialRates();
  }, []);

  // checkUser
  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    const checkUser = async () => {
      try {
        const response = await userService.checkSuperUser(user.name);
        if (response.status === 200) {
          setIsSuperUser(true);
        }
      } catch (error) {
        console.error("Error al verificar el SuperUser:", error);
      }
    }

    checkUser();
  }, []);

  // Envía los datos de la tarifa
  const handleRateSubmit = async () => {

    // Si el campo esta vacio, muestra un error
    const errors = {};
    if (!rateFormValues.code.trim()) errors.code = "El código es obligatorio.";
    if (!rateFormValues.price) errors.price = "El precio es obligatorio.";
    if (!rateFormValues.duration) errors.duration = "La duración es obligatoria.";
    if (!rateFormValues.description.trim()) errors.description = "La descripción es obligatoria.";
    setRateErrors(errors);
    if (Object.keys(errors).length > 0) return; // Si hay errores, no se envía el formulario

    try {
      await rateService.saveRate({
        code: rateFormValues.code,
        price: Number(rateFormValues.price),
        duration: Number(rateFormValues.duration),
        description: rateFormValues.description
      });
      setShowRateModal(false);
    } catch (error) {
      console.error("Error al guardar tarifa:", error);
    }
  }

  // Envía los datos del descuento
  const handleDiscountSubmit = async () => {

    // Si el campo esta vacio, muestra un error
    const errors = {};
    if (!discountFormValues.code.trim()) errors.code = "El código es obligatorio.";
    if (!discountFormValues.limInf) errors.limInf = "El límite inferior es obligatorio.";
    if (!discountFormValues.limSup) errors.limSup = "El límite superior es obligatorio.";
    if (!discountFormValues.discount) errors.discount = "El descuento es obligatorio.";
    if (!discountFormValues.description.trim()) errors.description = "La descripción es obligatoria.";
    setDiscountErrors(errors);
    if (Object.keys(errors).length > 0) return; // Si hay errores, no se envía el formulario

    try {
      await discountService.saveDiscount({
        code: discountFormValues.code,
        limInf: parseFloat(discountFormValues.limInf),
        limSup: parseFloat(discountFormValues.limSup),
        discount: parseFloat(discountFormValues.discount),
        description: discountFormValues.description
      });
      setShowDiscountModal(false);
    } catch (error) {
      console.error("Error al guardar descuento:", error);
    }
  }

  // Envía los datos del descuento frecuente
  const handleDiscountFreqSubmit = async () => {
    // Si el campo esta vacio, muestra un error
    const errors = {};
    if (!discountfreqFormValues.code.trim()) errors.code = "El código es obligatorio.";
    if (!discountfreqFormValues.limInf) errors.limInf = "El límite inferior es obligatorio.";
    if (!discountfreqFormValues.limSup) errors.limSup = "El límite superior es obligatorio.";
    if (!discountfreqFormValues.discount) errors.discount = "El descuento es obligatorio.";
    if (!discountfreqFormValues.description.trim()) errors.description = "La descripción es obligatoria.";
    setDiscountFreqErrors(errors);

    if (Object.keys(errors).length > 0) return; // Si hay errores, no se envía el formulario

    try {
      await discountService.saveDiscountFreq({
        code: discountfreqFormValues.code,
        limInf: parseFloat(discountfreqFormValues.limInf),
        limSup: parseFloat(discountfreqFormValues.limSup),
        discount: parseFloat(discountfreqFormValues.discount),
        description: discountfreqFormValues.description
      });
      setShowDiscountFreqModal(false);
    } catch (error) {
      console.error("Error al guardar descuento frecuente:", error);
    }
  }

  // Envía los datos de la tarifa especial
  const handleSpecialRateSubmit = async () => {
    // Si el campo esta vacio, muestra un error
    const errors = {};
    if (!specialRateFormValues.month) errors.month = "El mes es obligatorio.";
    if (!specialRateFormValues.day) errors.day = "El día es obligatorio.";
    if (!specialRateFormValues.discount) errors.discount = "El descuento es obligatorio.";
    if (!specialRateFormValues.description.trim()) errors.description = "La descripción es obligatoria.";
    setSpecialRateErrors(errors);
    if (Object.keys(errors).length > 0) return; // Si hay errores, no se envía el formulario
    try {
      await rateService.saveSpecialRate({
        month: parseInt(specialRateFormValues.month),
        day: parseInt(specialRateFormValues.day),
        discount: parseFloat(specialRateFormValues.discount),
        description: specialRateFormValues.description
      });
      setShowSpecialRateModal(false);
    } catch (error) {
      console.error("Error al guardar tarifa especial:", error);
    }
  }

  // Elimina la tarifa especial seleccionada
  const handleDeleteSpecialRate = async () => {
    const errors = {};
    if (!specialRateToDelete) errors.id = "El ID es obligatorio.";
    setSpecialRateToDeleteError(errors);
    if (Object.keys(errors).length > 0) return;
    try {
      await rateService.deleteSpecialRate(specialRateToDelete);
      setShowDeleteSpecialRateModal(false);
    } catch (error) {
      console.error("Error al eliminar tarifa especial:", error);
    }
  }

  // Elimina la tarifa seleccionada
  const handleDeleteRate = async () => {

    const errors = {};
    if (!rateToDelete) errors.code = "El código es obligatorio.";
    setRateToDeleteError(errors);
    if (Object.keys(errors).length > 0) return;

    try {
      await rateService.deleteRate(rateToDelete);
      setShowDeleteRateModal(false);
    } catch (error) {
      console.error("Error al eliminar tarifa:", error);
    }
  }

  // Elimina el descuento seleccionado
  const handleDeleteDiscount = async () => {

    const errors = {};
    if (!discountToDelete) errors.code = "El código es obligatorio.";
    setDiscountToDeleteError(errors);
    if (Object.keys(errors).length > 0) return;

    try {
      await discountService.deleteDiscount(discountToDelete);
      setShowDeleteDiscountModal(false);
    } catch (error) {
      console.error("Error al eliminar descuento:", error);
    }
  }

  // Elimina el descuento frecuente seleccionado
  const handleDeleteDiscountFreq = async () => {
    const errors = {};
    if (!discountFreqToDelete) errors.code = "El código es obligatorio.";
    setDiscountFreqToDeleteError(errors);
    if (Object.keys(errors).length > 0) return;
    try {
      await discountService.deleteDiscountFreq(discountFreqToDelete);
      setShowDeleteDiscountFreqModal(false);
    } catch (error) {
      console.error("Error al eliminar descuento frecuente:", error);
    }
  }

  // Envía los datos del kart
  const handleKartSubmit = async () => {
    const errors = {};
    if (!kartFormValues.name.trim()) errors.name = "El nombre es obligatorio.";
    if (!kartFormValues.mantentionDay.trim()) errors.mantentionDay = "El día de mantenimiento es obligatorio.";
    setShowKartError(errors);
    if (Object.keys(errors).length > 0) return;

    try {
      await kartService.saveKart({
        name: kartFormValues.name,
        mantentionDay: kartFormValues.mantentionDay
      });
      setShowKartModal(false);
    } catch (error) {
      console.error("Error al guardar kart:", error);
    }
  }

  // Elimina el kart seleccionado
  const handleDeleteKart = async () => {
    const errors = {};
    if (!kartToDelete) errors.name = "El nombre es obligatorio.";
    setKartToDeleteError(errors);
    if (Object.keys(errors).length > 0) return;
    try {
      await kartService.deleteKart(kartToDelete);
      setShowDeleteKartModal(false);
    } catch (error) {
      console.error("Error al eliminar kart:", error);
    }
  }

  const fetchIncomeData = async () => {
    setLoading(true);
    setError(null);
  
    try {
      let response;
      if (tipoTabla === 'vueltas') {
        response = await reservationService.getIncomePerRate(fechaInicio, fechaFin);
      } else if (tipoTabla === 'grupo') {
        response = await reservationService.getIncomePerGroup(fechaInicio, fechaFin);
      }
  
      setIncomeData(response.data);
      setShowModal(true);      
    } catch (error) {
      setError(error.response?.data?.message || 'Error al obtener datos');
    } finally {
      setLoading(false);
    }
  };

  const fetchReservedHours = async (date) => {
    try {
      const response = await reservationService.findHoursReserved(format(date, 'yyyy-MM-dd'));
      setReservedHours(response.data);
    } catch (error) {
      console.error("Error al obtener las horas reservadas:", error);
    }
  };

  const actualizeReservedHours = async (date) => {
    try {
      await reservationService.actualizeReservedHours(format(date, 'yyyy-MM-dd'));
      console.log("Horas reservadas actualizadas para la fecha:", format(date, 'yyyy-MM-dd'));
    } catch (error) {
      console.error("Error al actualizar las horas reservadas:", error);
    }
  };

  const handleDayClick = async (date) => {
    const day = format(date, 'eeee', { locale: es });
    const capitalizedDay = day.charAt(0).toUpperCase() + day.slice(1);

    // envia el date al backend para que tenga sus horas reservedas actualizadas
    await actualizeReservedHours(date);

    // consigue las horas reservadas para el día seleccionado
    fetchReservedHours(date);

    const diasValidos = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];
  
    if (diasValidos.includes(capitalizedDay)) {
      setSelectedDay(capitalizedDay);
      setSelectedDate(format(date, 'yyyy-MM-dd'));
      setShowDayModal(true);
    }
  };

  useEffect(() => {
    const fetchAvailableKarts = async () => {
      if (selectedDay) {
        try {
          const response = await kartService.countAvailableKarts(selectedDay); // Enviamos el string del día
          setAvailableKarts(response.data);
        } catch (error) {
          console.error("Error al contar los karts disponibles:", error);
        }
      }
    };
  
    fetchAvailableKarts();
  }, [selectedDay]);

  const handleOpenReservaModal = (hora) => {
    setSelectedHour(hora);
    setShowReservaModal(true);
  };

  const handleMakeReservation = async () => {

    const errors = {};
    if (!formReservation.clientId) errors.clientId = "El ID del cliente es obligatorio.";
    if (!formReservation.rateCode) errors.rateCode = "El código de tarifa es obligatorio.";
    setReservationErrors(errors);

    if (Object.keys(errors).length > 0) return;

    formReservation.hourChoosen = selectedHour;
    formReservation.dateChoosen = selectedDate;

    try {
      console.log("Realizando reserva con los siguientes datos:", formReservation);
      console.log("Hora seleccionada:", selectedHour);
      await reservationService.makeReservation(
        formReservation.clientId,
        formReservation.companionsId,
        formReservation.rateCode,
        selectedHour,
        formReservation.dateChoosen
      );
      alert("Reserva realizada exitosamente");
      setShowReservaModal(false);
    } catch (error) {
      console.error("Error al realizar la reserva", error);
      alert("Hubo un error al realizar la reserva.");
    }
  };


  return (
    <div className="d-flex justify-content-center align-items-start p-4">
      <Card style={{ width: '90%', minHeight: '80vh' }} className="shadow">
        <Card.Body>
          {/* Estas son las pestañas */}
          <Tabs
            id="menu-tabs"
            activeKey={activeTab}
            onSelect={(k) => {
              setActiveTab(k);
              localStorage.setItem('activeTab', k); // Guarda la pestaña seleccionada
            }}
            className="mb-3"
          >
            {/* Pestaña con todo lo relacionado a horarios*/}
            <Tab eventKey="horario" title="Horario">
              {/* Selector de semana */}
              <div className="d-flex align-items-center justify-content-center mb-4" style={{ gap: '1rem' }}>
                <Button variant="outline-secondary" onClick={goToPreviousWeek}>
                  «
                </Button>
                <h4 className="mb-0 text-center">
                  {format(currentDate, "MMMM yyyy", { locale: es })}
                </h4>
                <Button variant="outline-secondary" onClick={goToNextWeek}>
                  »
                </Button>
              </div>

              {/* Semana actual */}
              <div
                className="grid text-center mb-3"
                style={{
                  display: 'grid',
                  gridTemplateColumns: 'repeat(7, 1fr)',
                  gap: '10px',
                }}
              >
                {/* Por cada día en date muestra un bloque*/}
                {weekDates.map((date) => (
                  <div key={date} className="p-2 border rounded">
                    <div style={{ fontSize: '0.85rem', color: '#666' }}>
                      {format(date, 'EEE', { locale: es })} {/* Nombre del día */}
                    </div>
                    {/* Botón para abrir las horas del día*/}
                    <Button
                      variant="outline-primary"
                      className="mt-1"
                      style={{ fontSize: '1.3rem' }}
                      onClick={() => handleDayClick(date)}
                    >
                      {format(date, 'd')}
                      <div style={{ fontSize: '0.7rem' }}>Horas disponibles</div>
                    </Button>
                  </div>
                ))}
              </div>
            </Tab>

            {/* Pestaña de prueba */}
            <Tab eventKey="prueba" title="Prueba">
              <p>Este es un texto de prueba en la pestaña secundaria.</p>
            </Tab>

            {/* Pestaña de administración, solo visible para superusuarios */}
            {isSuperUser && (
              <Tab eventKey="admin" title="Tarifas, Tarifas Especiales y Karts">
                <div className="mt-3">

                  {/* Primera linea de contenido de la pestaña */}
                  <div style={{ display: 'flex', gap: '20px', flexWrap: 'wrap',}}>

                    {/* Contenido de tarifas */}
                    <div style={{ flex: 1 }}> 

                      {/* Titulo de tarifas */}
                      <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <h4>Tarifas</h4>
                      </div>

                      {/* Tabla de tarifas */}
                      <div style={{ maxHeight: '140px', overflowY: 'auto' }}>
                        <Table striped bordered hover size="sm">
                          <thead>
                            <tr>
                              <th>Código</th>
                              <th>Precio</th>
                              <th>Duración</th>
                              <th>Descripción</th>
                            </tr>
                          </thead>
                          <tbody>
                            {rates.map((rate) => (
                              <tr key={rate.code}>
                                <td>{rate.code}</td>
                                <td>${rate.price}</td>
                                <td>{rate.duration} minutos</td>
                                <td>{rate.description}</td>
                              </tr>
                            ))}
                          </tbody>
                        </Table>
                      </div>

                      {/* Div que pone de lado los botones */}
                      <div style={{ display: 'flex', justifyContent: 'center', gap: '10px', flexWrap: 'wrap' }}>
                        {/* Botón para abrir el modal de tarifas */}
                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <Button
                            className="mt-2"
                            onClick={() => {
                              setRateFormValues({ code: '', price: '', duration: '', description: '' });
                              setShowRateModal(true);
                            }}> 
                            Guardar Tarifas
                          </Button>
                        </div>

                        {/* Botón para eliminar tarifas */}
                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <Button
                            className="mt-2"
                            variant="danger"
                            onClick={() => {
                              setRateToDelete(rateFormValues.code);
                              setShowDeleteRateModal(true);
                            }}>Eliminar Tarifas
                          </Button>
                        </div>
                      </div>
                    </div>

                    {/* Contenido de tarifas especiales */}
                    <div style={{ flex: 1 }}>
                      <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <h4>Tarifas Especiales</h4>
                      </div>

                      <div style={{ maxHeight: '140px', overflowY: 'auto' }}>
                        <Table striped bordered hover size="sm">
                          <thead>
                            <tr>
                              <th>Id</th>
                              <th>Mes</th>
                              <th>Día</th>
                              <th>Descuento</th>
                              <th>Descripción</th>
                            </tr>
                          </thead>
                          <tbody>
                            {specialRates.map((specialRate) => (
                              <tr key={specialRate.id}>
                                <td>{specialRate.id}</td>
                                <td>{monthOrder[specialRate.month - 1]}</td>
                                <td>{specialRate.day}</td>
                                <td>{specialRate.discount * 100}%</td>
                                <td>{specialRate.description}</td>
                              </tr>
                            ))}
                          </tbody>
                        </Table>
                      </div>

                      <div style={{ display: 'flex', justifyContent: 'center', gap: '10px', flexWrap: 'wrap' }}>
                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <Button
                            className="mt-2"
                            onClick={() => {
                              setSpecialRateFormValues({ month: '', day: '', discount: '', description: '' });
                              setShowSpecialRateModal(true);
                            }}> Guardar Tarifa Especial
                            </Button>
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <Button
                            className="mt-2"
                            variant="danger"
                            onClick={() => {
                              setSpecialRateToDelete(specialRateFormValues.id);
                              setShowDeleteSpecialRateModal(true);
                            }}>Eliminar Tarifa Especial
                            </Button>
                        </div>
                      </div>

                    </div>
                  </div>


                  {/* Segunda linea de contenido de la pestaña */}
                  <div style={{ display: 'flex', gap: '20px', flexWrap: 'wrap',}}>

                    {/* Contenido de karts */}
                    <div style={{ flex: 1 }}>

                      <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <h4>Karts</h4>
                      </div>

                      <div style={{ maxHeight: '140px', overflowY: 'auto' }}>
                        <Table striped bordered hover size="sm">
                          <thead>
                            <tr>
                              <th>Nombre</th>
                              <th>Día de Mantenimiento</th>
                            </tr>
                          </thead>
                          <tbody>
                            {karts.map((kart) => (
                              <tr key={kart.name}>
                                <td>{kart.name}</td>
                                <td>{kart.mantentionDay}</td>
                              </tr>
                            ))}
                          </tbody>
                        </Table>
                      </div>

                      {/* Botón para abrir el modal de karts */}
                      <div style={{ display: 'flex', justifyContent: 'center', gap: '10px', flexWrap: 'wrap' }}>
                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <Button
                            className="mt-2"
                            onClick={() => {
                              setKartFormValues({ name: '', mantentionDay: '' });
                              setShowKartModal(true);
                            }}>Guardar Karts
                          </Button>
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <Button
                            className="mt-2"
                            variant="danger"
                            onClick={() => {
                              setKartToDelete(kartFormValues.name);
                              setShowDeleteKartModal(true);
                            }}>Eliminar Karts
                          </Button>
                        </div>


                      </div>
                    </div>

                    <div style={{ flex: 1, marginTop: '40px', flexDirection: 'column' }}>

                      <div>
                        Para modificar una instancia de tarifa, descuento o kart, simplemente haga click en guardar, ingrese el nombre o código
                        correspondiente e ingrese la información a modificar.
                      </div>


                      <Button className="mt-2" variant="secondary" onClick={() => window.location.reload()}
                        style={{ maxWidth: '200px', width: '100%', justifyContent: 'center', display: 'flex' }}>
                        Refrescar Página
                      </Button>
                    </div>
                  </div>

                </div>
              </Tab>
            )}

            {  /* Pestaña de descuentos */}
            {isSuperUser && (
              <Tab eventKey="descuentos" title="Descuentos">
                {/* Primera linea de contenido de la pestaña */}
                  <div style={{ display: 'flex', gap: '20px', flexWrap: 'wrap',}}>

                    {/* Contenido de descuentos Num */}
                    <div style={{ flex: 1 }}>
                      <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <h4>Descuentos</h4>
                      </div>

                      <div style={{ maxHeight: '140px', overflowY: 'auto' }}>
                        <Table striped bordered hover size="sm">
                          <thead>
                            <tr>
                              <th>Código</th>
                              <th>Límite Inferior</th>
                              <th>Límite Superior</th>
                              <th>Descuento</th>
                              <th>Descripción</th>
                            </tr>
                          </thead>
                          <tbody>
                            {discounts.map((discount) => (
                              <tr key={discount.code}>
                                <td>{discount.code}</td>
                                <td>{discount.limInf}</td>
                                <td>{discount.limSup}</td>
                                <td>{discount.discount * 100}%</td>
                                <td>{discount.description}</td>
                              </tr>
                            ))}
                          </tbody>
                        </Table>
                      </div>

                      <div style={{ display: 'flex', justifyContent: 'center', gap: '10px', flexWrap: 'wrap' }}>
                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <Button
                            className="mt-2"
                            onClick={() => {
                              setDiscountFormValues({ code: '', limInf: '', limSup: '', discount: '', description: '' });
                              setShowDiscountModal(true);
                            }}> Guardar Descuentos
                            </Button>
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <Button
                            className="mt-2"
                            variant="danger"
                            onClick={() => {
                              setDiscountToDelete(discountFormValues.code);
                              setShowDeleteDiscountModal(true);
                            }}>Eliminar Descuentos
                            </Button>
                        </div>
                      </div>
                    </div>

                    {/* Contenido de descuentos Frecuentes */}
                    <div style={{ flex: 1 }}>
                      <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <h4>Descuentos Frecuentes</h4>
                      </div>

                      <div style={{ maxHeight: '140px', overflowY: 'auto' }}>
                        <Table striped bordered hover size="sm">
                          <thead>
                            <tr>
                              <th>Código</th>
                              <th>Límite Inferior</th>
                              <th>Límite Superior</th>
                              <th>Descuento</th>
                              <th>Descripción</th>
                            </tr>
                          </thead>
                          <tbody>
                            {discountsFreq.map((discount) => (
                              <tr key={discount.code}>
                                <td>{discount.code}</td>
                                <td>{discount.limInf}</td>
                                <td>{discount.limSup}</td>
                                <td>{discount.discount * 100}%</td>
                                <td>{discount.description}</td>
                              </tr>
                            ))}
                          </tbody>
                        </Table>
                      </div>

                      <div style={{ display: 'flex', justifyContent: 'center', gap: '10px', flexWrap: 'wrap' }}>
                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <Button
                            className="mt-2"
                            onClick={() => {
                              setDiscountFreqFormValues({ code: '', limInf: '', limSup: '', discount: '', description: '' });
                              setShowDiscountFreqModal(true);
                            }}> Guardar Descuentos Frecuentes
                            </Button>
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <Button
                            className="mt-2"
                            variant="danger"
                            onClick={() => {
                              setDiscountFreqToDelete(discountfreqFormValues.code);
                              setShowDeleteDiscountFreqModal(true);
                            }}>Eliminar Descuentos Frecuentes
                            </Button>
                        </div>
                      </div>
                    </div>
                  </div>
              </Tab>


            )}

            {isSuperUser && (
              <Tab eventKey="reportes" title="Reportes2025">
                <div className="mt-3" style={{ display: 'flex', flexDirection: 'column', gap: '20px', maxWidth: '600px' }}>
                  
                  <div>
                    <label>Fecha de inicio</label>
                    <input
                      type="date"
                      className="form-control"
                      value={fechaInicio}
                      onChange={(e) => setFechaInicio(e.target.value)}
                    />
                  </div>

                  <div>
                    <label>Fecha de fin</label>
                    <input
                      type="date"
                      className="form-control"
                      value={fechaFin}
                      onChange={(e) => setFechaFin(e.target.value)}
                    />
                  </div>

                  <div>
                    <label>Tipo de tabla</label>
                    <Form.Select
                      className="form-control"
                      value={tipoTabla}
                      onChange={(e) => setTipoTabla(e.target.value)}
                    >
                      <option value="">Seleccione una opción</option>
                      <option value="vueltas">Ingresos por N° de Vueltas o Tiempo en Pista</option>
                      <option value="grupo">Ingresos por Tamaño de Grupo</option>
                    </Form.Select>
                  </div>

                  <Button
                    onClick={fetchIncomeData}
                    disabled={!fechaInicio || !fechaFin || !tipoTabla || loading}
                  >
                    {loading ? 'Cargando...' : 'Generar Reporte'}
                  </Button>

                </div>
              </Tab>
            )}
            
          </Tabs>
        </Card.Body>
      </Card>

      <Modal show={showDayModal} onHide={() => setShowDayModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>
            <div>
              Horas para el día {selectedDay ?? ''}
            </div>
            <div>
              {availableKarts !== null ? `${availableKarts} karts disponibles` : 'Cargando...'}
            </div>
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <ListGroup>
            {/* en caso de que la hora este reservada, no estara disponible*/}
            {horas.map((hora) => {
              // Comparación segura (convertimos las horas reservadas a formato HH:MM para comparar)
              const estaReservada = reservedHours.some(
                reservada => reservada.slice(0, 5) === hora
              );

              return (
                <div
                  key={hora}
                  className="d-flex align-items-center justify-content-center mb-2"
                >
                  <span className="me-3">{hora}</span>
                  
                  {estaReservada ? (
                    <span className="badge bg-secondary">Reservado</span>
                  ) : (
                    <Button
                      variant="primary"
                      size="sm"
                      onClick={() => {
                        setFormReservation({ clientId: '', companionsId: [], rateCode: '' });
                        handleOpenReservaModal(hora);
                      }}
                    >
                      Reservar
                    </Button>
                  )}
                </div>
              );
            })}


            
            
          </ListGroup>
        </Modal.Body>
      </Modal>

      {/* Modal para editar tarifas */}
      <Modal show={showRateModal} onHide={() => setShowRateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Modificar Tarifa</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Código</Form.Label>
              <Form.Control
                type="text"
                value={rateFormValues.code}
                onChange={(e) => setRateFormValues({ ...rateFormValues, code: e.target.value })}
                isInvalid={!!rateErrors.code}
              />
              <Form.Control.Feedback type="invalid">{rateErrors.code}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Precio</Form.Label>
              <Form.Control
                type="number"
                value={rateFormValues.price}
                onChange={(e) => setRateFormValues({ ...rateFormValues, price: e.target.value })}
                isInvalid={!!rateErrors.price}
              />
              <Form.Control.Feedback type="invalid">{rateErrors.price}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Duración (minutos)</Form.Label>
              <Form.Control
                type="number"
                value={rateFormValues.duration}
                onChange={(e) => setRateFormValues({ ...rateFormValues, duration: e.target.value })}
                isInvalid={!!rateErrors.duration}
              />
              <Form.Control.Feedback type="invalid">{rateErrors.duration}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Descripción</Form.Label>
              <Form.Control
                type="text"
                value={rateFormValues.description}
                onChange={(e) => setRateFormValues({ ...rateFormValues, description: e.target.value })}
                isInvalid={!!rateErrors.description}
              />
              <Form.Control.Feedback type="invalid">{rateErrors.description}</Form.Control.Feedback>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowRateModal(false)}>
            Cancelar
          </Button>
          <Button variant="primary" onClick={handleRateSubmit}>
            Guardar Cambios
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal para editar descuentos */}
      <Modal show={showDiscountModal} onHide={() => setShowDiscountModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Modificar Descuento</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Código</Form.Label>
              <Form.Control
                type="text"
                value={discountFormValues.code}
                onChange={(e) => setDiscountFormValues({ ...discountFormValues, code: e.target.value })}
                isInvalid={!!discountErrors.code}
              />
              <Form.Control.Feedback type="invalid">{discountErrors.code}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Límite Inferior</Form.Label>
              <Form.Control
                type="number"
                value={discountFormValues.limInf}
                onChange={(e) => setDiscountFormValues({ ...discountFormValues, limInf: e.target.value })}
                isInvalid={!!discountErrors.limInf}
              />
              <Form.Control.Feedback type="invalid">{discountErrors.limInf}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Límite Superior</Form.Label>
              <Form.Control
                type="number"
                value={discountFormValues.limSup}
                onChange={(e) => setDiscountFormValues({ ...discountFormValues, limSup: e.target.value })}
                isInvalid={!!discountErrors.limSup}
              />
              <Form.Control.Feedback type="invalid">{discountErrors.limSup}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Descuento (%)</Form.Label>
              <Form.Control
                type="number"
                value={discountFormValues.discount}
                onChange={(e) => setDiscountFormValues({ ...discountFormValues, discount: e.target.value })}
                isInvalid={!!discountErrors.discount}
              />
              <Form.Control.Feedback type="invalid">{discountErrors.discount}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Descripción</Form.Label>
              <Form.Control
                type="text"
                value={discountFormValues.description}
                onChange={(e) => setDiscountFormValues({ ...discountFormValues, description: e.target.value })}
                isInvalid={!!discountErrors.description}
              />
              <Form.Control.Feedback type="invalid">{discountErrors.description}</Form.Control.Feedback>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDiscountModal(false)}>
            Cancelar
          </Button>
          <Button variant="primary" onClick={handleDiscountSubmit}>
            Guardar Cambios
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal para eliminar tarifas */}
      <Modal show={showDeleteRateModal} onHide={() => setShowDeleteRateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Eliminar Tarifa</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Código de la tarifa a eliminar</Form.Label>
              <Form.Control
                type="text"
                value={rateToDelete}
                onChange={(e) => setRateToDelete(e.target.value)}
                isInvalid={!!rateToDeleteError.code}
              />
              <Form.Control.Feedback type="invalid">{rateToDeleteError.code}</Form.Control.Feedback>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteRateModal(false)}>
            Cancelar
          </Button>
          <Button variant="danger" onClick={handleDeleteRate}>
            Eliminar
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal para editar descuentos frecuentes */}
      <Modal show={showDiscountFreqModal} onHide={() => setShowDiscountFreqModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Modificar Descuento Frecuente</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Código</Form.Label>
              <Form.Control
                type="text"
                value={discountfreqFormValues.code}
                onChange={(e) => setDiscountFreqFormValues({ ...discountfreqFormValues, code: e.target.value })}
                isInvalid={!!discountfreqErrors.code} 
              />
              <Form.Control.Feedback type="invalid">{discountfreqErrors.code}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Límite Inferior</Form.Label>
              <Form.Control
                type="number"
                value={discountfreqFormValues.limInf}
                onChange={(e) => setDiscountFreqFormValues({ ...discountfreqFormValues, limInf: e.target.value })}
                isInvalid={!!discountfreqErrors.limInf}
              />
              <Form.Control.Feedback type="invalid">{discountfreqErrors.limInf}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Límite Superior</Form.Label>
              <Form.Control
                type="number"
                value={discountfreqFormValues.limSup}
                onChange={(e) => setDiscountFreqFormValues({ ...discountfreqFormValues, limSup: e.target.value })}
                isInvalid={!!discountfreqErrors.limSup}
              />
              <Form.Control.Feedback type="invalid">{discountfreqErrors.limSup}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Descuento (%)</Form.Label>
              <Form.Control
                type="number"
                value={discountfreqFormValues.discount}
                onChange={(e) => setDiscountFreqFormValues({ ...discountfreqFormValues, discount: e.target.value })}
                isInvalid={!!discountfreqErrors.discount}
              />
              <Form.Control.Feedback type="invalid">{discountfreqErrors.discount}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Descripción</Form.Label>
              <Form.Control
                type="text"
                value={discountfreqFormValues.description}
                onChange={(e) => setDiscountFreqFormValues({ ...discountfreqFormValues, description: e.target.value })}
                isInvalid={!!discountfreqErrors.description}
              />
              <Form.Control.Feedback type="invalid">{discountfreqErrors.description}</Form.Control.Feedback>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDiscountFreqModal(false)}>
            Cancelar
          </Button>
          <Button variant="primary" onClick={handleDiscountFreqSubmit}>
            Guardar Cambios
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal para eliminar descuentos */}
      <Modal show={showDeleteDiscountModal} onHide={() => setShowDeleteDiscountModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Eliminar Descuento</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Código del descuento a eliminar</Form.Label>
              <Form.Control
                type="text"
                value={discountToDelete}
                onChange={(e) => setDiscountToDelete(e.target.value)}
                isInvalid={!!discountToDeleteError.code}
              />
              <Form.Control.Feedback type="invalid">{discountToDeleteError.code}</Form.Control.Feedback>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteDiscountModal(false)}>
            Cancelar
          </Button>
          <Button variant="danger" onClick={handleDeleteDiscount}>
            Eliminar
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal para eliminar descuentos frecuentes */}
      <Modal show={showDeleteDiscountFreqModal} onHide={() => setShowDeleteDiscountFreqModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Eliminar Descuento Frecuente</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Código del descuento frecuente a eliminar</Form.Label>
              <Form.Control
                type="text"
                value={discountFreqToDelete}
                onChange={(e) => setDiscountFreqToDelete(e.target.value)}
                isInvalid={!!discountFreqToDeleteError.code}
              />
              <Form.Control.Feedback type="invalid">{discountFreqToDeleteError.code}</Form.Control.Feedback>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteDiscountFreqModal(false)}>
            Cancelar
          </Button>
          <Button variant="danger" onClick={handleDeleteDiscountFreq}>
            Eliminar
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal para editar karts */}
      <Modal show={showKartModal} onHide={() => setShowKartModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Modificar Kart</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Nombre</Form.Label>
              <Form.Control
                type="text"
                value={kartFormValues.name}
                onChange={(e) => setKartFormValues({ ...kartFormValues, name: e.target.value })}
                isInvalid={!!showKartError.name}
              />
              <Form.Control.Feedback type="invalid">{showKartError.name}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Día de Mantenimiento</Form.Label>
              <Form.Control
                type="text"
                value={kartFormValues.mantentionDay}
                onChange={(e) => setKartFormValues({ ...kartFormValues, mantentionDay: e.target.value })}
                isInvalid={!!showKartError.mantentionDay}
              />
              <Form.Control.Feedback type="invalid">{showKartError.mantentionDay}</Form.Control.Feedback>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowKartModal(false)}>
            Cancelar
          </Button>
          <Button variant="primary" onClick={handleKartSubmit}>
            Guardar Cambios
          </Button>
        </Modal.Footer>
      </Modal> 

      {/* Modal para eliminar karts */}
      <Modal show={showDeleteKartModal} onHide={() => setShowDeleteKartModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Eliminar Kart</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Nombre del kart a eliminar</Form.Label>
              <Form.Control

                type="text"
                value={kartToDelete}
                onChange={(e) => setKartToDelete(e.target.value)}
                isInvalid={!!kartToDeleteError.name}
              />
              <Form.Control.Feedback type="invalid">{kartToDeleteError.name}</Form.Control.Feedback>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteKartModal(false)}>
            Cancelar
          </Button>
          <Button variant="danger" onClick={handleDeleteKart}>
            Eliminar
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal para editar tarifas especiales */}
      <Modal show={showSpecialRateModal} onHide={() => setShowSpecialRateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Modificar Tarifa Especial</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Mes</Form.Label>
              <Form.Control
                type="number"
                value={specialRateFormValues.month}
                onChange={(e) => setSpecialRateFormValues({ ...specialRateFormValues, month: e.target.value })}
                isInvalid={!!specialRateErrors.month}
              />
              <Form.Control.Feedback type="invalid">{specialRateErrors.month}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Día</Form.Label>
              <Form.Control
                type="number"
                value={specialRateFormValues.day}
                onChange={(e) => setSpecialRateFormValues({ ...specialRateFormValues, day: e.target.value })}
                isInvalid={!!specialRateErrors.day}
              />
              <Form.Control.Feedback type="invalid">{specialRateErrors.day}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Descuento (%)</Form.Label>
              <Form.Control
                type="number"
                value={specialRateFormValues.discount}
                onChange={(e) => setSpecialRateFormValues({ ...specialRateFormValues, discount: e.target.value })}
                isInvalid={!!specialRateErrors.discount}
              />
              <Form.Control.Feedback type="invalid">{specialRateErrors.discount}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Descripción</Form.Label>
              <Form.Control
                type="text"
                value={specialRateFormValues.description}
                onChange={(e) => setSpecialRateFormValues({ ...specialRateFormValues, description: e.target.value })}
                isInvalid={!!specialRateErrors.description}
              />
              <Form.Control.Feedback type="invalid">{specialRateErrors.description}</Form.Control.Feedback>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowSpecialRateModal(false)}>
            Cancelar
          </Button>
          <Button variant="primary" onClick={handleSpecialRateSubmit}>
            Guardar Cambios
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal para eliminar tarifas especiales */}
      <Modal show={showDeleteSpecialRateModal} onHide={() => setShowDeleteSpecialRateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Eliminar Tarifa Especial</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>ID de la tarifa especial a eliminar</Form.Label>
              <Form.Control
                type="text"
                value={specialRateToDelete}
                onChange={(e) => setSpecialRateToDelete(e.target.value)}
                isInvalid={!!specialRateToDeleteError.id}
              />
              <Form.Control.Feedback type="invalid">{specialRateToDeleteError.id}</Form.Control.Feedback>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteSpecialRateModal(false)}>
            Cancelar
          </Button>
          <Button variant="danger" onClick={handleDeleteSpecialRate}>
            Eliminar
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal para mostrar los reportes */}
      <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Reporte de Ingresos</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {error && <Alert variant="danger">{error}</Alert>}

          {incomeData && tipoTabla === 'vueltas' && (() => {
            const monthlyList = incomeData.monthlyIncomeList || [];
            const allMonths = [...new Set(monthlyList.map(entry => entry.month.toUpperCase()))];
            const orderedMonths = monthOrder.filter(m => allMonths.includes(m));
            const rateCodes = Object.keys(incomeData.totalIncomePerRate || {});
            const orderedRateCodes = orderRateCodes(rateCodes);

            return (
              <Table striped bordered hover size="sm">
                <thead>
                  <tr>
                    <th>Tipo de Tarifa</th>
                    {orderedMonths.map(month => (
                      <th key={month}>{month}</th>
                    ))}
                    <th>Total por Tarifa</th>
                  </tr>
                </thead>
                <tbody>
                  {orderedRateCodes.map(rateCode => (
                    <tr key={rateCode}>
                      <td>{rateDescriptions[rateCode] || rateCode}</td>
                      {orderedMonths.map(month => {
                        const entry = monthlyList.find(
                          e => e.month.toUpperCase() === month && e.rateCode === rateCode
                        );
                        return (
                          <td key={month}>
                            {entry ? entry.cellValue.toFixed(2) : "0.00"}
                          </td>
                        );
                      })}
                      <td>{(incomeData.totalIncomePerRate[rateCode] || 0).toFixed(2)}</td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr>
                    <td><strong>Total por Mes</strong></td>
                    {orderedMonths.map(month => (
                      <td key={month}>
                        <strong>{(incomeData.totalIncomePerMonth[month] || 0).toFixed(2)}</strong>
                      </td>
                    ))}
                    <td><strong>{(incomeData.totalIncome || 0).toFixed(2)}</strong></td>
                  </tr>
                </tfoot>
              </Table>
            );
          })()}

          {incomeData && tipoTabla === 'grupo' && (() => {
            const monthlyList = incomeData.monthlyIncomeList || [];
            const allMonths = [...new Set(monthlyList.map(entry => entry.month.toUpperCase()))];
            const orderedMonths = monthOrder.filter(m => allMonths.includes(m));
            const groupCodes = Object.keys(incomeData.totalIncomePerGroup || {});
            const orderedGroupCodes = orderRateCodes(groupCodes); // usamos la misma función para ordenar por número

            return (
              <Table striped bordered hover size="sm">
                <thead>
                  <tr>
                    <th>Tipo de Grupo</th>
                    {orderedMonths.map(month => (
                      <th key={month}>{month}</th>
                    ))}
                    <th>Total por Grupo</th>
                  </tr>
                </thead>
                <tbody>
                  {orderedGroupCodes.map(groupCode => (
                    <tr key={groupCode}>
                      <td>{groupDescriptions[groupCode] || groupCode}</td>
                      {orderedMonths.map(month => {
                        const entry = monthlyList.find(
                          e => e.month.toUpperCase() === month && e.groupCode === groupCode
                        );
                        return (
                          <td key={month}>
                            {entry ? entry.cellValue.toFixed(2) : "0.00"}
                          </td>
                        );
                      })}
                      <td>{(incomeData.totalIncomePerGroup[groupCode] || 0).toFixed(2)}</td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr>
                    <td><strong>Total por Mes</strong></td>
                    {orderedMonths.map(month => (
                      <td key={month}>
                        <strong>{(incomeData.totalIncomePerMonth[month] || 0).toFixed(2)}</strong>
                      </td>
                    ))}
                    <td><strong>{(incomeData.totalIncome || 0).toFixed(2)}</strong></td>
                  </tr>
                </tfoot>
              </Table>
            );
          })()}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cerrar
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={showReservaModal} onHide={() => setShowReservaModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Confirmar Reserva - {selectedHour}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>ID del Cliente</Form.Label>
            <Form.Control
              type="number"
              value={formReservation.clientId}
              onChange={(e) => setFormReservation({ ...formReservation, clientId: parseInt(e.target.value) })}
              isInvalid={!!reservationErrors.clientId}
            />
            <Form.Control.Feedback type="invalid">{reservationErrors.clientId}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>IDs de Acompañantes (separados por coma)</Form.Label>
            <Form.Control
              type="text"
              value={formReservation.companionsInput || ''}
              onChange={(e) => setFormReservation({
                ...formReservation,
                companionsInput: e.target.value,
                companionsId: e.target.value
                  .split(',')
                  .map(id => parseInt(id.trim()))
                  .filter(id => !isNaN(id))
              })}
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Código de Tarifa</Form.Label>
            <Form.Control
              type="text"
              value={formReservation.rateCode}
              onChange={(e) => setFormReservation({ ...formReservation, rateCode: e.target.value })}
              isInvalid={!!reservationErrors.rateCode}
            />
            <Form.Control.Feedback type="invalid">{reservationErrors.rateCode}</Form.Control.Feedback>
          </Form.Group>

          <Button variant="primary" onClick={handleMakeReservation}>
            Reservar
          </Button>
        </Form>
        </Modal.Body>
      </Modal>
      

    </div>
  );
};

export default Menu;