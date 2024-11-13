import React, { useState } from 'react';
import { Bell, LogOut, Home } from "lucide-react"; // Home 아이콘 추가
import ProfileModal from './ProfileModal.jsx';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅 추가
import '../css/Header.css';

const Header = ({ isLoggedIn, setIsLoggedIn, user }) => {
    const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);
    const navigate = useNavigate(); // useNavigate 훅 사용

    const handleAvatarUpload = () => {
        setIsProfileModalOpen(true);
    };

    const handleLogout = () => {
        setIsLoggedIn(false);
        localStorage.removeItem('accessToken'); // 로그아웃 시 토큰 제거
        localStorage.removeItem('refreshToken');
        navigate('/login'); // 로그아웃 후 로그인 페이지로 이동
    };

    // user.avatar에서 파일명 추출하여 URL 생성
    const avatarFileName = user.avatar ? user.avatar.split('/').pop() : null;

    // 기본 아바타 이미지 URL 설정
    const defaultAvatarUrl = 'http://localhost:8000/images/default-avatar.png';

    // avatarUrl을 설정할 때, user.avatar가 유효한 경우에만 URL을 생성
    const avatarUrl = avatarFileName && !user.avatar.includes('default-avatar.png')
        ? `http://localhost:8000/${avatarFileName}`
        : defaultAvatarUrl;

    return (
        <>
            <header className="header bg-blue-600 text-white">
                <div className="container mx-auto px-4 py-4 flex justify-between items-center">
                    <h1 className="text-2xl font-bold">국민동의청원 알리미</h1>
                    <nav className="flex items-center">
                        <ul className="flex space-x-4 items-center">
                            <li>
                                <Home
                                    className="w-6 h-6 hover:text-blue-200 cursor-pointer"
                                    onClick={() => navigate('/home')} // 클릭 시 홈 페이지로 이동
                                />
                            </li>
                            <li>
                                <button onClick={() => navigate('/petitions')} className="hover:text-blue-200 text-white">전체 청원</button>
                            </li>
                            {isLoggedIn ? (
                                <>
                                    <li>
                                        <button onClick={() => navigate('/inquiries')} className="hover:text-blue-200 text-white">1:1 문의</button>
                                    </li>
                                    <li>
                                        <Bell className="w-6 h-6 hover:text-blue-200 cursor-pointer text-white" />
                                    </li>
                                    {/* 아바타 이미지와 사용자 이름 */}
                                    <li className="flex items-center mr-4">
                                        <img
                                            src={avatarUrl} // 실제 아바타 이미지 URL
                                            alt="User Avatar"
                                            className="w-8 h-8 rounded-full border-2 border-gray-300 cursor-pointer mr-2"
                                            onClick={() => window.open(avatarUrl, '_blank')} // 클릭 시 링크로 이동
                                        />
                                        <span
                                            className="text-white cursor-pointer"
                                            onClick={() => navigate('/memberInfo')} // 사용자 이름 클릭 시 memberInfo 페이지로 이동
                                        >
                                            {user.name}
                                        </span>
                                    </li>
                                    <li>
                                        <button
                                            onClick={handleLogout}
                                            className="flex items-center hover:text-blue-200 text-white"
                                        >
                                            <LogOut className="w-6 h-6 mr-1" />
                                            로그아웃
                                        </button>
                                    </li>
                                </>
                            ) : (
                                <li>
                                    <button onClick={() => navigate('/login')} className="hover:text-blue-200 text-white">로그인</button>
                                </li>
                            )}
                        </ul>
                    </nav>
                </div>
            </header>

            {isProfileModalOpen && (
                <ProfileModal
                    user={user}
                    setIsProfileModalOpen={setIsProfileModalOpen}
                />
            )}
        </>
    );
};

export default Header;
