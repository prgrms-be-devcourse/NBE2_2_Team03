import React, { useEffect, useState } from 'react';

const ExpiringPetitions = () => {
    const [petitions, setPetitions] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchExpiringPetitions = async () => {
            try {
                const response = await fetch('http://localhost:8000/api/petitions/view/endDate', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                if (!response.ok) {
                    console.error(`Error: ${response.status} ${response.statusText}`);
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
                console.error('Error fetching expiring petitions:', error);
                setError('청원 데이터를 가져오는 데 실패했습니다.');
            } finally {
                setIsLoading(false);
            }
        };

        fetchExpiringPetitions();
    }, []);

    return (
        <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-xl font-semibold mb-4 text-gray-800">곧 종료되는 청원</h2>
            {isLoading && <p>로딩 중...</p>}
            {error && <p className="text-red-500">{error}</p>}
            <ul>
                {petitions.length === 0 ? (
                    <p>데이터가 없습니다.</p>
                ) : (
                    petitions.map((petition, index) => (
                        <li key={petition.petitionId} className="mb-4 p-4 border-b last:border-b-0">
                            <h3 className="text-lg font-semibold text-black">{index + 1}. {petition.title}</h3> {/* 순위 추가 */}
                            <p className="text-gray-600">카테고리: {petition.category}</p>
                            <p className="text-gray-600">시작일: {new Date(petition.startDate).toLocaleDateString()}</p>
                            <p className="text-gray-600">만료일: {new Date(petition.endDate).toLocaleDateString()}</p>
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

export default ExpiringPetitions;
