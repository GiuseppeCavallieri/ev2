import httpClient from "../http-common";

const userService = {
    login: (name, pass) => {
        return httpClient.post("/user/login", { name, pass });
    },

    register: (name, pass, email, birthday, superuser) => {
        return httpClient.post("/user/register", { superuser, name, pass, email, birthday });
    },

    checkSuperUser: (name) => {
        return httpClient.get(`/user/superuser/${name}`);
    }
};


export default userService;