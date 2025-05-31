import httpClient from "../http-common";

const kartService = {

    saveKart: (kart) => {
        return httpClient.post("/kart/saveKart", kart);
    },

    getAllKarts: () => {
        return httpClient.get("/kart/getAll");
    },

    countAvailableKarts: (mantentionDay) => {
        return httpClient.get(`/kart/countAvailableKarts/${mantentionDay}`);
    },

    getAvailableKartsByDay: (Day) => {
        return httpClient.get(`/kart/getAvailableKarts/${Day}`);
    },

    deleteKart: (kartCode) => {
        return httpClient.delete(`/kart/deleteKart/${kartCode}`);
    }

};

export default kartService;