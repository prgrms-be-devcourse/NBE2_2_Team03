import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import axios from 'axios';
import Header from './components/Header.jsx';
import HomePage from './components/HomePage.jsx';
import ProfileModal from './components/ProfileModal.jsx';
import InquiriesPage from './components/InquiriesPage.jsx';
import AllPetitionsPage from './components/AllPetitionsPage.jsx';
import LoginPage from './components/LoginPage.jsx';
import SignUpPage from './components/SignUpPage.jsx';
import MemberInfo from './components/MemberInfo.jsx';
import Footer from './components/Footer.jsx';

const App = () => {
    const [currentPage, setCurrentPage] = useState('home');
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState({ username: 'user', avatar: 'default-avatar.png' });
    const [inquiries, setInquiries] = useState([
        { id: 1, question: "청원 제출 방법에 대해 알고 싶습니다.", answer: "청원은 홈페이지의 '청원하기' 버튼을 통해 제출하실 수 있습니다.", date: "2024-03-15", status: "답변완료" },
        { id: 2, question: "내 청원이 왜 반려되었나요?", answer: null, date: "2024-03-20", status: "접수" },
        { id: 3, question: "비밀번호를 잊어버렸어요. 어떻게 재설정하나요?", answer: "비밀번호 재설정은 로그인 페이지의 '비밀번호 찾기' 링크를 통해 가능합니다.", date: "2024-03-22", status: "답변완료" },
    ]);

    const [petitions, setPetitions] = useState([
        { id: 1, title: "청원 제목 1", description: "청원 설명 1", category: "카테고리 1", date: "2024-01-01", status: "진행 중" },
        { id: 2, title: "청원 제목 2", description: "청원 설명 2", category: "카테고리 2", date: "2024-01-02", status: "진행 중" },
        { id: 3, title: "청원 제목 3", description: "청원 설명 3", category: "카테고리 3", date: "2024-01-03", status: "종료" },
    ]);
    const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);

    const handleProfileUpdate = (updatedUser) => {
        setUser(updatedUser);
        setIsProfileModalOpen(false);
    };

    useEffect(() => {
        const fetchPetitions = async () => {
            try {
                const response = await axios.get('http://localhost:8000/api/petitions');
                setPetitions(response.data);
            } catch (error) {
                console.error('Error fetching petitions:', error);
            }
        };

        fetchPetitions();
    }, []);

    return (
        <Router>
            <div className="min-h-screen flex flex-col bg-gray-100">
                <Header
                    isLoggedIn={isLoggedIn}
                    setIsLoggedIn={setIsLoggedIn}
                    user={user}
                    setIsProfileModalOpen={setIsProfileModalOpen}
                />
                <main className="flex-grow container mx-auto px-4 py-8">
                    <Routes>
                        <Route path="/" element={<HomePage petitions={petitions} />} />
                        <Route path="/home" element={<HomePage petitions={petitions} />} />
                        <Route path="/login" element={<LoginPage setIsLoggedIn={setIsLoggedIn} setUser={setUser} />} />
                        <Route path="/signup" element={<SignUpPage />} />
                        <Route path="/inquiries" element={isLoggedIn ? <InquiriesPage inquiries={inquiries} setInquiries={setInquiries} /> : <LoginPage setIsLoggedIn={setIsLoggedIn} setUser={setUser} />} />
                        <Route path="/petitions" element={<AllPetitionsPage petitions={petitions} />} />
                        <Route path="/memberInfo" element={<MemberInfo user={user} setUser={setUser} />} />
                    </Routes>
                </main>
                {isProfileModalOpen && (
                    <ProfileModal
                        user={user}
                        onUpdate={handleProfileUpdate}
                        setIsProfileModalOpen={setIsProfileModalOpen}
                    />
                )}
                <Footer />
            </div>
        </Router>
    );
};

export default App;
