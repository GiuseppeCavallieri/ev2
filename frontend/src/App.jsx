import './App.css'
import { Routes, Route } from "react-router-dom";
import LoginMenu from './components/LoginMenu';
import RegisterMenu from "./components/RegisterMenu";
import Menu from "./components/Menu";

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<LoginMenu/>} />
        <Route path="/register" element={<RegisterMenu />} />
        <Route path="/Menu" element={<Menu />} />
      </Routes>
    </div>
  )
}

export default App
