import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const SignUpPage = () => {
    const [formData, setFormData] = useState({
        userId: '',
        name: '',
        email: '',
        password: '',
        phone: '',
        role: 'USER',
    });
    const [isSuccess, setIsSuccess] = useState(false); // 회원가입 성공 상태 추가
    const [errorMessage, setErrorMessage] = useState(''); // 오류 메시지 상태 추가
    const navigate = useNavigate(); // useNavigate 훅 사용

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch('http://localhost:8000/api/members/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('회원가입 실패');
                }
                return response.json();
            })
            .then(data => {
                console.log('회원가입 성공:', data);
                setIsSuccess(true); // 성공 상태 변경
            })
            .catch(error => {
                console.error('회원가입 중 오류 발생:', error);
                setErrorMessage('회원가입에 실패했습니다. 다시 시도해 주세요.'); // 오류 메시지 설정
            });
    };

    const handleClosePopup = () => {
        setIsSuccess(false);
        navigate('/login'); // 로그인 페이지로 이동
    };

    return (
        <div className="p-6">
            <h1 className="text-2xl font-semibold mb-4 text-gray-800">회원가입</h1>
            <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md">
                <label className="block mb-2 text-gray-800">
                    사용자 ID:
                    <input
                        type="text"
                        name="userId"
                        value={formData.userId}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md bg-white text-black"
                        required
                    />
                </label>
                <label className="block mb-2 text-gray-800">
                    이름:
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md bg-white text-black"
                        required
                    />
                </label>
                <label className="block mb-2 text-gray-800">
                    이메일:
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md bg-white text-black"
                        required
                    />
                </label>
                <label className="block mb-2 text-gray-800">
                    비밀번호:
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md bg-white text-black"
                        required
                    />
                </label>
                <label className="block mb-2 text-gray-800">
                    전화번호:
                    <input
                        type="tel"
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md bg-white text-black"
                        required
                    />
                </label>
                <label className="block mb-2 text-gray-800">
                    역할:
                    <select
                        name="role"
                        value={formData.role}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md bg-white text-black"
                    >
                        <option value="USER">사용자</option>
                        <option value="ADMIN">관리자</option>
                    </select>
                </label>
                <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700">가입하기</button>
            </form>

            {/* 성공 메시지 팝업 */}
            {isSuccess && (
                <div className="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-50">
                    <div className="bg-white p-6 rounded-lg shadow-md w-80">
                        <h2 className="text-lg font-semibold mb-4 text-black">회원가입 성공!</h2>
                        <p className="text-black">회원가입에 성공하였습니다. 이제 로그인하여 사용해보세요!</p>
                        <button onClick={handleClosePopup} className="mt-4 w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700">
                            로그인하러 가기
                        </button>
                    </div>
                </div>
            )}

            {/* 오류 메시지 표시 */}
            {errorMessage && <p className="text-red-500 mt-4">{errorMessage}</p>}
        </div>
    );
};

export default SignUpPage;
