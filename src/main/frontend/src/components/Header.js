import React, { useState } from 'react';
import { Bell, LogOut, User } from "lucide-react";
import ProfileModal from './ProfileModal'; // Import the ProfileModal component

const Header = ({ setCurrentPage, isLoggedIn, setIsLoggedIn, user, setUser }) => {
    const [isProfileModalOpen, setIsProfileModalOpen] = useState(false); // State to manage modal visibility

    const handleAvatarUpload = () => {
        setIsProfileModalOpen(true); // Open the profile modal when avatar is clicked
    };

    const handleUpdate = (updatedUser) => {
        setUser(updatedUser); // Update the user state with the new user data
        setIsProfileModalOpen(false); // Close the modal after updating
    };

    return (
        <>
            <header className="bg-blue-600 text-white shadow-md">
                <div className="container mx-auto px-4 py-4 flex justify-between items-center">
                    <h1 className="text-2xl font-bold">국민동의청원 알리미</h1>
                    <nav>
                        <ul className="flex space-x-4 items-center">
                            <li><button onClick={() => setCurrentPage('home')} className="hover:text-blue-200">홈</button></li>
                            <li><button onClick={() => setCurrentPage('petitions')} className="hover:text-blue-200">전체 청원</button></li>
                            {isLoggedIn ? (
                                <>
                                    <li><button onClick={() => setCurrentPage('inquiries')} className="hover:text-blue-200">1:1 문의</button></li>
                                    <li><Bell className="w-6 h-6 hover:text-blue-200 cursor-pointer" /></li>
                                    <li className="flex items-center cursor-pointer">
                                        <img
                                            src={user.avatar}
                                            alt="User Avatar"
                                            className="w-8 h-8 rounded-full mr-2"
                                            onClick={handleAvatarUpload} // Open the profile modal on avatar click
                                        />
                                        <User className="w-6 h-6 hover:text-blue-200" onClick={() => setCurrentPage('memberInfo')} />
                                    </li>
                                    <li>
                                        <button
                                            onClick={() => {
                                                setIsLoggedIn(false);
                                                setCurrentPage('home');
                                            }}
                                            className="flex items-center hover:text-blue-200"
                                        >
                                            <LogOut className="w-6 h-6 mr-1" />
                                            로그아웃
                                        </button>
                                    </li>
                                </>
                            ) : (
                                <li><button onClick={() => setCurrentPage('login')} className="hover:text-blue-200">로그인</button></li>
                            )}
                        </ul>
                    </nav>
                </div>
            </header>

            {isProfileModalOpen && (
                <ProfileModal
                    user={user}
                    onUpdate={handleUpdate}
                    setIsProfileModalOpen={setIsProfileModalOpen} // Optional: if you want to close it from inside the modal
                />
            )}
        </>
    );
};

export default Header;
