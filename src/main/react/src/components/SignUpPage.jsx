import React, { useState } from 'react';

const SignUpPage = () => {
    const [formData, setFormData] = useState({
        userId: '',
        name: '',
        email: '',
        password: '',
        phone: '',
        role: 'USER', // 기본 역할 설정 (예: USER)
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // 회원가입 API 호출
        fetch('http://localhost:8000/api/members/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        })
            .then(response => response.json())
            .then(data => {
                // 가입 성공 후 처리
                console.log('Member signed up:', data);
            })
            .catch(error => console.error('Error signing up:', error));
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
        </div>
    );
};

export default SignUpPage;
