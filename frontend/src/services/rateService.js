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
};


export default rateService;