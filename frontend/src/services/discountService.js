import httpClient from "../http-common";

const discountService = {
    getAllDiscounts: () => {
        return httpClient.get("/discountnum/getAllDiscounts");
    },

    getAllDiscountsFreq: () => {
        return httpClient.get("/discountfreq/getAllDiscounts");
    },

    saveDiscount: (discount) => {
        return httpClient.post("/discountnum/save", discount);
    },

    saveDiscountFreq: (discount) => {
        return httpClient.post("/discountfreq/save", discount);
    },

    deleteDiscount: (code) => {
        return httpClient.delete(`/discountnum/delete/${code}`);
    },

    deleteDiscountFreq: (code) => {
        return httpClient.delete(`/discountfreq/delete/${code}`);
    },
};


export default discountService;