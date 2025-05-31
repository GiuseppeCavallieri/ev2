import httpClient from "../http-common";

const rateService = {
    getAllRates: () => {
        return httpClient.get("/rate/getAll");
    },

    saveRate: (rate) => {
        return httpClient.post("/rate/save", rate);
    },

    deleteRate: (code) => {
        return httpClient.delete(`/rate/delete/${code}`);
    },

    getAllSpecialRates: () => {
        return httpClient.get("/specialrate/all");
    },

    saveSpecialRate: (rate) => {
        return httpClient.post("/specialrate/save", rate);
    },

    deleteSpecialRate: (code) => {
        return httpClient.delete(`/specialrate/delete/${code}`);
    }
};


export default rateService;