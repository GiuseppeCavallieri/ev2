import httpClient from "../http-common";

const discountService = {
    getAllDiscounts: () => {
        return httpClient.get("/discount/getAll");
    },

    saveDiscount: (discount) => {
        return httpClient.post("/discount/save", discount);
    },

    deleteDiscount: (code) => {
        return httpClient.delete(`/discount/delete/${code}`);
    },
};


export default discountService;