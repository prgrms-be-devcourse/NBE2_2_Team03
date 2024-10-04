import React from 'react';

const AllPetitionsPage = ({ petitions }) => {
    return (
        <div className="p-6">
            <h2 className="text-2xl font-semibold mb-4 text-gray-800">전체 청원</h2> {/* 제목 텍스트 색상 변경 */}
            <ul>
                {petitions.map((petition) => (
                    <li key={petition.id} className="mb-4 p-4 bg-white rounded shadow">
                        <h3 className="text-xl font-semibold text-gray-800">{petition.title}</h3> {/* 제목 텍스트 색상 변경 */}
                        <p className="text-gray-700">{petition.description}</p> {/* 설명 텍스트 색상 변경 */}
                        <p className="text-gray-600">카테고리: {petition.category}</p> {/* 카테고리 텍스트 색상 변경 */}
                        <p className="text-gray-600">날짜: {petition.date}</p> {/* 날짜 텍스트 색상 변경 */}
                        <p className="text-gray-600">상태: {petition.status}</p> {/* 상태 텍스트 색상 변경 */}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default AllPetitionsPage;
