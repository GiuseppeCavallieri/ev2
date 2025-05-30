import axios from "axios";

const booKartsServer = "localhost:8080";

export default axios.create({
    baseURL: `http://${booKartsServer}`,
    headers: {
        'Content-Type': 'application/json'
    } 
});