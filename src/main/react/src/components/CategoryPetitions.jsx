import React, { useEffect, useState } from 'react';

// 카테고리 목록
const categories = [
    { value: 'POLITICS', label: '정치/선거/국회운영' },
    { value: 'INVESTIGATION', label: '수사/법무/사법제도' },
    { value: 'FINANCE', label: '재정/세제/금융/예산' },
    { value: 'CONSUMER', label: '소비자/공정거래' },
    { value: 'EDUCATION', label: '교육' },
    { value: 'SCIENCE', label: '과학기술/정보통신' },
    { value: 'DIPLOMACY', label: '외교/통일/국방/안보' },
    { value: 'DISASTER', label: '재난/안전/환경' },
    { value: 'ADMINISTRATION', label: '행정/지방자치' },
    { value: 'CULTURE', label: '문화/체육/관광/언론' },
    { value: 'AGRICULTURE', label: '농업/임업/수산업/축산업' },
    { value: 'INDUSTRY', label: '산업/통상' },
    { value: 'HEALTHCARE', label: '보건의료' },
    { value: 'WELFARE', label: '복지/보훈' },
    { value: 'LAND', label: '국토/해양/교통' },
    { value: 'HUMAN', label: '인권/성평등/노동' },
    { value: 'LOW_BIRTHRATE', label: '저출산/고령화/아동/청소년/가족' },
    { value: 'OTHERS', label: '기타' },
];

const CategoryPetitions = () => {
    const [petitions, setPetitions] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [selectedCategory, setSelectedCategory] = useState(categories[0].value); // 기본 카테고리 설정

    useEffect(() => {
        const fetchCategoryPetitions = async () => {
            try {
                const response = await fetch(`http://localhost:8000/api/petitions/view/category/${selectedCategory}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                if (!response.ok) {
                    throw new Error('네트워크 응답이 좋지 않습니다.');
                }
                // const data = await response.json();
                // setPetitions(data); // 전체 데이터 설정
                const apiResponse = await response.json();
                const { success, data, message } = apiResponse; // ApiResponse 구조에 맞게 구조 분해

                if (success) {
                    setPetitions(data); // 성공적으로 데이터가 있으면 전체 데이터 설정
                } else {
                    console.error('Error:', message); // 에러 처리
                    setError(message); // 백엔드에서 받은 에러 메시지를 상태에 설정
                }
            } catch (error) {
                console.error('Error fetching category petitions:', error);
                setError('청원 데이터를 가져오는 데 실패했습니다.');
            } finally {
                setIsLoading(false);
            }
        };

        fetchCategoryPetitions();
    }, [selectedCategory]); // 카테고리 변경 시 데이터 재요청

    return (
        <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-xl font-semibold mb-4 text-gray-800">카테고리별 청원</h2>
            <select
                value={selectedCategory}
                onChange={(e) => setSelectedCategory(e.target.value)}
                className="mb-4 border rounded p-2 bg-gray-200 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500" // 배경색 및 글자색 변경
            >
                {categories.map((category) => (
                    <option key={category.value} value={category.value}>
                        {category.label}
                    </option>
                ))}
            </select>
            {isLoading && <p>로딩 중...</p>}
            {error && <p className="text-red-500">{error}</p>}
            <ul>
                {petitions.length === 0 ? (
                    <p>데이터가 없습니다.</p>
                ) : (
                    petitions.map((petition, index) => (
                        <li key={petition.petitionId} className="mb-2">
                            <h3 className="text-lg font-semibold text-black">{index + 1}. {petition.title}</h3>
                            <p className="text-gray-600">카테고리: {petition.category}</p>
                            <p className="text-gray-600">좋아요 수: {petition.likesCount}</p>
                            <p className="text-gray-600">관심 수: {petition.interestCount}</p>
                            <p className="text-gray-600">동의 수: {petition.agreeCount}</p>
                        </li>
                    ))
                )}
            </ul>
        </div>
    );
};

export default CategoryPetitions;
