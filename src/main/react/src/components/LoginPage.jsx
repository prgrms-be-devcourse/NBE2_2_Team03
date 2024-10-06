import React, { useState } from 'react';
import axios from 'axios';

const LoginPage = ({ setIsLoggedIn, setCurrentPage, setUser }) => {
    const [userId, setUserId] = useState(''); // userId 상태 추가
    const [password, setPassword] = useState(''); // 비밀번호 상태 추가
    const [error, setError] = useState(''); // 에러 메시지 상태 추가

    const handleLogin = async (e) => {
        e.preventDefault();
        console.log("로그인 요청 시작");
        try {
            const response = await axios.post('http://localhost:8000/api/members/login', { userId, password });
            console.log("서버 응답 데이터:", response.data);

            // JWT 토큰을 로컬 스토리지에 저장
            const { accessToken, refreshToken } = response.data.data;
            console.log("Access Token:", accessToken);
            console.log("Refresh Token:", refreshToken);

            // JWT 토큰 저장
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);

            // 로그인 성공 시 사용자 정보 요청
            await fetchUserInfo(accessToken); // 사용자 정보 요청

            // 사용자 정보 요청이 완료된 후, 홈 페이지로 이동
            setIsLoggedIn(true);
            setCurrentPage('home'); // 홈 페이지로 이동

        } catch (err) {
            console.error("로그인 에러:", err.response?.data || err.message);
            setError('로그인에 실패했습니다. 아이디와 비밀번호를 확인하세요.');
        }
    };

    // 사용자 정보 요청 함수
    const fetchUserInfo = async (token) => {
        try {
            const response = await axios.get('http://localhost:8000/api/members/protected-data', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            const { userId, name, email, phone, avatarImage, role, memberId } = response.data.data; // userData를 가져옵니다.
            console.log("User Data:", { userId, name, email, phone, avatarImage, role, memberId }); // 추가된 로그
            setUser({ userId, name, email, phone, avatar: avatarImage || 'default-avatar.png', role, memberId }); // user 상태 업데이트
        } catch (error) {
            console.error("사용자 정보 요청 실패:", error);
        }
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen p-6">
            <h2 className="text-3xl font-bold mb-6 text-gray-800">로그인</h2>
            {error && <p className="text-red-500 mb-4">{error}</p>} {/* 에러 메시지 표시 */}
            <form onSubmit={handleLogin} className="bg-white p-6 rounded-lg shadow-md">
                <div className="mb-4">
                    <label className="block mb-2 text-gray-800">아이디 (userId)</label>
                    <input
                        type="text"
                        className="w-full p-2 border border-gray-300 rounded-md bg-white text-black"
                        value={userId} // userId 상태 연결
                        onChange={(e) => setUserId(e.target.value)} // 상태 업데이트
                        required
                    />
                </div>
                <div className="mb-4">
                    <label className="block mb-2 text-gray-800">비밀번호</label>
                    <input
                        type="password"
                        className="w-full p-2 border border-gray-300 rounded-md bg-white text-black"
                        value={password} // 비밀번호 상태 연결
                        onChange={(e) => setPassword(e.target.value)} // 상태 업데이트
                        required
                    />
                </div>
                <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700">로그인</button>
                <p className="mt-4 text-center text-gray-700">
                    계정이 없으신가요?
                    <span onClick={() => setCurrentPage('signup')} className="text-blue-600 cursor-pointer">회원가입</span>
                </p>
            </form>
        </div>
    );
};

export default LoginPage;
