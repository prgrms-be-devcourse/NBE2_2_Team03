import React, { useState } from 'react';
import Header from './components/Header';
import HomePage from './components/HomePage';
import ProfileModal from './components/ProfileModal';
import InquiriesPage from './components/InquiriesPage';
import AllPetitionsPage from './components/AllPetitionsPage';
import LoginPage from './components/LoginPage';
import SignUpPage from './components/SignUpPage';
import MemberInfo from "./components/MemberInfo";
import Footer from "./components/Footer";

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

    return (
        <div className="min-h-screen bg-gray-100">
            <Header
                setCurrentPage={setCurrentPage}
                isLoggedIn={isLoggedIn}
                setIsLoggedIn={setIsLoggedIn}
                user={user}
                setIsProfileModalOpen={setIsProfileModalOpen}
            />
            <main className="container mx-auto px-4 py-8">
                {currentPage === 'home' && <HomePage />}
                {currentPage === 'login' && <LoginPage setIsLoggedIn={setIsLoggedIn} setCurrentPage={setCurrentPage} />}
                {currentPage === 'inquiries' && isLoggedIn && <InquiriesPage inquiries={inquiries} setInquiries={setInquiries} />}
                {currentPage === 'petitions' && <AllPetitionsPage petitions={petitions} />}
                {currentPage === 'signup' && <SignUpPage />} {/* SignUpPage 렌더링 */}
                {currentPage === 'memberInfo' && <MemberInfo />}
                {isProfileModalOpen && <ProfileModal user={user} onUpdate={handleProfileUpdate} />}
            </main>
            <Footer />
        </div>
    );
};

export default App;
