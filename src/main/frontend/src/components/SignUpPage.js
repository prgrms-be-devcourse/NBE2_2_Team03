import React, { useState } from 'react';

const SignUpPage = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        phone: '',
        // 추가 필드가 있다면 여기에 추가
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // 회원가입 API 호출
        fetch('/api/signup', {
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
        <div>
            <h1>회원가입</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    이메일:
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                    />
                </label>
                <label>
                    비밀번호:
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                    />
                </label>
                <label>
                    전화번호:
                    <input
                        type="tel"
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                    />
                </label>
                <button type="submit">가입하기</button>
            </form>
        </div>
    );
};

export default SignUpPage;
