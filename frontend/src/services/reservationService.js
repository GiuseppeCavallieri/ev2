import httpClient from "../http-common";

const reservationService = {

    makeReservation: (clientId, companionsId, rateCode, hourChoosen, dateChoosen) => {
        return httpClient.post("/reservation/save", { clientId, companionsId, rateCode, hourChoosen, dateChoosen });
    },

    findHoursReserved: (date) => {
        return httpClient.get(`/rack/findByDate/${date}`);
    },

    getIncomePerRate: (startDate, endDate) => {
        return httpClient.get(`/reservation/getIncomePerRate/${startDate}/${endDate}`);
    },

    getIncomePerGroup: (startDate, endDate) => {
        return httpClient.get(`/reservation/getIncomePerGroup/${startDate}/${endDate}`);
    },

    actualizeReservedHours: (date) => {
        return httpClient.get(`/rack/saveRacks/${date}`);
    }
};


export default reservationService;