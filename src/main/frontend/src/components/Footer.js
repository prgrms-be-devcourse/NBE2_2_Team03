import React from "react";

const Footer = () => {
    return (
        <footer className="bg-gray-800 text-white py-4 text-center">
            <p>&copy; {new Date().getFullYear()} 국민동의청원 알리미</p>
        </footer>
    );
};

export default Footer;