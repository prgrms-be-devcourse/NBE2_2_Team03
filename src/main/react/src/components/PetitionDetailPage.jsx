import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

const PetitionDetailPage = () => {
    const { petitionId } = useParams();
    const navigate = useNavigate(); // useNavigate 훅 사용
    const [petition, setPetition] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPetition = async () => {
            try {
                const response = await axios.get(`http://localhost:8000/api/petitions/${petitionId}`);
                const { success, data, message } = response.data; // ApiResponse 구조에 맞게 구조 분해

                if (success) {
                    setPetition(data); // 성공적으로 데이터 설정
                } else {
                    console.error('Error:', message); // 에러 처리
                    setError(message); // 백엔드에서 받은 에러 메시지를 상태에 설정
                }
            } catch (error) {
                console.error('Error fetching petition:', error);
                setError('청원 상세 데이터를 가져오는 데 실패했습니다.'); // 일반적인 에러 메시지 설정
            } finally {
                setIsLoading(false);
            }
        };

        fetchPetition();
    }, [petitionId]);

    if (isLoading) {
        return <p className="text-black">로딩 중...</p>;
    }

    if (error) {
        return <p className="text-red-500">{error}</p>;
    }

    if (!petition) {
        return <p className="text-black">청원 데이터가 없습니다.</p>;
    }

    return (
        <div className="p-6">
            <h2 className="text-2xl font-semibold mb-4 text-black">{petition.title}</h2>

            <p className="text-black"><strong>시작일:</strong> {new Date(petition.startDate).toLocaleDateString()}</p>
            <p className="text-black"><strong>만료일:</strong> {new Date(petition.endDate).toLocaleDateString()}</p>
            <p className="text-black"><strong>카테고리:</strong> {petition.category.name}</p>
            <p className="text-black"><strong>내용:</strong> {petition.content}</p>
            <p className="text-black"><strong>요약:</strong> {petition.summary}</p>
            <p className="text-black"><strong>좋아요 수:</strong> {petition.likesCount}</p>
            <p className="text-black"><strong>관심 수:</strong> {petition.interestCount}</p>
            <p className="text-black"><strong>동의 수:</strong> {petition.agreeCount}</p>
            <p className="text-black"><strong>원본 URL:</strong> <a href={petition.originalUrl} target="_blank" rel="noopener noreferrer" className="text-blue-600 hover:underline">{petition.originalUrl}</a></p>
            <p className="text-black mb-6"><strong>관련 뉴스:</strong> {petition.relatedNews}</p> {/* 관련 뉴스에 마진 추가 */}

            <div className="flex justify-center mb-4">
                <button
                    onClick={() => navigate(-1)} // 이전 페이지로 돌아가기
                    className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
                >
                    이전 페이지로 돌아가기
                </button>
            </div>
        </div>
    );
};

export default PetitionDetailPage;
