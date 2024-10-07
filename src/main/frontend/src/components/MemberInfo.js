import React, { useEffect, useState } from 'react';

const MemberInfoPage = () => {
    const [memberInfo, setMemberInfo] = useState({
        email: '',
        phone: '',
        // 다른 정보가 있다면 여기에 추가
    });

    useEffect(() => {
        // 회원 정보 가져오기
        fetch('/api/member/info') // 실제 API 경로에 맞춰 수정
            .then(response => response.json())
            .then(data => setMemberInfo(data))
            .catch(error => console.error('Error fetching member info:', error));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setMemberInfo({ ...memberInfo, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // 회원 정보 수정 API 호출
        fetch('/api/member/update', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(memberInfo),
        })
            .then(response => response.json())
            .then(data => {
                // 수정 성공 후 처리
                console.log('Member info updated:', data);
            })
            .catch(error => console.error('Error updating member info:', error));
    };

    return (
        <div>
            <h1>회원 정보</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    이메일:
                    <input
                        type="email"
                        name="email"
                        value={memberInfo.email}
                        onChange={handleChange}
                    />
                </label>
                <label>
                    전화번호:
                    <input
                        type="tel"
                        name="phone"
                        value={memberInfo.phone}
                        onChange={handleChange}
                    />
                </label>
                {/* 다른 정보 필드가 있다면 여기에 추가 */}
                <button type="submit">수정하기</button>
            </form>
        </div>
    );
};

export default MemberInfoPage;
