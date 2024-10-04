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
        <div className="p-6">
            <h1 className="text-2xl font-semibold mb-4 text-gray-800">회원 정보</h1> {/* 텍스트 색상 변경 */}
            <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md">
                <label className="block mb-2 text-gray-800">이메일:</label> {/* 텍스트 색상 변경 */}
                <input
                    type="email"
                    name="email"
                    value={memberInfo.email}
                    onChange={handleChange}
                    className="w-full p-2 border border-gray-300 rounded-md bg-white text-black mb-4" // 흰색 배경에 검정색 텍스트
                />
                <label className="block mb-2 text-gray-800">전화번호:</label> {/* 텍스트 색상 변경 */}
                <input
                    type="tel"
                    name="phone"
                    value={memberInfo.phone}
                    onChange={handleChange}
                    className="w-full p-2 border border-gray-300 rounded-md bg-white text-black mb-4" // 흰색 배경에 검정색 텍스트
                />
                {/* 다른 정보 필드가 있다면 여기에 추가 */}
                <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700">수정하기</button>
            </form>
        </div>
    );
};

export default MemberInfoPage;
