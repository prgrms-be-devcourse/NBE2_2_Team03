import React from 'react';

const LoginPage = ({ setIsLoggedIn, setCurrentPage }) => {
    const handleLogin = (e) => {
        e.preventDefault();
        // Perform login logic here...
        setIsLoggedIn(true);
        setCurrentPage('home');
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen p-6">
            <h2 className="text-3xl font-bold mb-6 text-gray-800">로그인</h2> {/* 텍스트 색상 변경 */}
            <form onSubmit={handleLogin} className="bg-white p-6 rounded-lg shadow-md">
                <div className="mb-4">
                    <label className="block mb-2 text-gray-800">아이디</label> {/* 텍스트 색상 변경 */}
                    <input
                        type="text"
                        className="w-full p-2 border border-gray-300 rounded-md bg-white text-black" // 흰색 배경에 검정색 텍스트
                        required
                    />
                </div>
                <div className="mb-4">
                    <label className="block mb-2 text-gray-800">비밀번호</label> {/* 텍스트 색상 변경 */}
                    <input
                        type="password"
                        className="w-full p-2 border border-gray-300 rounded-md bg-white text-black" // 흰색 배경에 검정색 텍스트
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
