import React from 'react';

const AllPetitionsPage = ({ petitions }) => {
    return (
        <div>
            <h2 className="text-2xl font-semibold mb-4">전체 청원</h2>
            <ul>
                {petitions.map((petition) => (
                    <li key={petition.id} className="mb-4 p-4 bg-white rounded shadow">
                        <h3 className="text-xl font-semibold">{petition.title}</h3>
                        <p>{petition.description}</p>
                        <p>카테고리: {petition.category}</p>
                        <p>날짜: {petition.date}</p>
                        <p>상태: {petition.status}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default AllPetitionsPage;
