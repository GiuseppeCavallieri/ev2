import httpClient from "../http-common";

const reservationService = {

    makeReservation: (clientId, companionsId, rateCode, hourChoosen, dateChoosen) => {
        return httpClient.post("/reservation/save", { clientId, companionsId, rateCode, hourChoosen, dateChoosen });
    },

    findHoursReserved: (date) => {
        return httpClient.get(`/reservation/findHoursReserved/${date}`);
    },

    getIncomePerRate: (startDate, endDate) => {
        return httpClient.get(`/reservation/getIncomePerRate/${startDate}/${endDate}`);
    },
    getIncomePerGroup: (startDate, endDate) => {
        return httpClient.get(`/reservation/getIncomePerGroup/${startDate}/${endDate}`);
    }
};


export default reservationService;