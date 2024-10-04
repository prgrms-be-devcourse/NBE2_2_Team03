import React, { useState } from 'react';
import { Bell, LogOut, User } from "lucide-react";
import ProfileModal from './ProfileModal.jsx'; // Import the ProfileModal component
import '../css/Header.css'; // CSS 파일 임포트

const Header = ({ setCurrentPage, isLoggedIn, setIsLoggedIn, user, setUser }) => {
    const [isProfileModalOpen, setIsProfileModalOpen] = useState(false); // State to manage modal visibility

    const handleAvatarUpload = () => {
        setIsProfileModalOpen(true); // Open the profile modal when avatar is clicked
    };

    const handleUpdate = (updatedUser) => {
        setUser(updatedUser); // Update the user state with the new user data
        setIsProfileModalOpen(false); // Close the modal after updating
    };

    const handleLogout = () => {
        setIsLoggedIn(false); // Set logged in state to false
        setCurrentPage('home'); // Redirect to home page
    };

    return (
        <>
            <header className="header bg-blue-600 text-white"> {/* 배경색과 텍스트 색상 설정 */}
                <div className="container mx-auto px-4 py-4 flex justify-between items-center">
                    <h1 className="text-2xl font-bold">국민동의청원 알리미</h1>
                    <nav>
                        <ul className="flex space-x-4 items-center">
                            <li>
                                <button onClick={() => setCurrentPage('home')} className="hover:text-blue-200 text-white">홈</button>
                            </li>
                            <li>
                                <button onClick={() => setCurrentPage('petitions')} className="hover:text-blue-200 text-white">전체 청원</button>
                            </li>
                            {isLoggedIn ? (
                                <>
                                    <li>
                                        <button onClick={() => setCurrentPage('inquiries')} className="hover:text-blue-200 text-white">1:1 문의</button>
                                    </li>
                                    <li>
                                        <Bell className="w-6 h-6 hover:text-blue-200 cursor-pointer text-white" />
                                    </li>
                                    <li className="flex items-center cursor-pointer">
                                        <img
                                            src={user.avatar}
                                            alt="User Avatar"
                                            className="w-8 h-8 rounded-full mr-2"
                                            onClick={handleAvatarUpload} // Open the profile modal on avatar click
                                        />
                                        <User className="w-6 h-6 hover:text-blue-200 text-white" onClick={() => setCurrentPage('memberInfo')} />
                                    </li>
                                    <li>
                                        <button
                                            onClick={handleLogout} // Use the handleLogout function
                                            className="flex items-center hover:text-blue-200 text-white"
                                        >
                                            <LogOut className="w-6 h-6 mr-1" />
                                            로그아웃
                                        </button>
                                    </li>
                                </>
                            ) : (
                                <li>
                                    <button onClick={() => setCurrentPage('login')} className="hover:text-blue-200 text-white">로그인</button>
                                </li>
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
