import httpClient from "../http-common";

const reservationService = {

    makeReservation: (clientId, companionsId, rateCode, hourChoosen, dateChoosen) => {
        return httpClient.post("/reservation/makeReservation", { clientId, companionsId, rateCode, hourChoosen, dateChoosen });
    },

    getIncomePerRate: (startDate, endDate) => {
        return httpClient.get(`/reservation/getIncomePerRate/${startDate}/${endDate}`);
    },
    getIncomePerGroup: (startDate, endDate) => {
        return httpClient.get(`/reservation/getIncomePerGroup/${startDate}/${endDate}`);
    }
};


export default reservationService;