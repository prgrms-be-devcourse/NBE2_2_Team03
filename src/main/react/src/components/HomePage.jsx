import React, { useState } from 'react';
import SearchBar from './SearchBar.jsx';
import TopPetitions from './TopPetitions.jsx';
import ExpiringPetitions from './ExpiringPetitions.jsx';
import CategoryPetitions from './CategoryPetitions.jsx';

const HomePage = () => {
    const [petitions, setPetitions] = useState([]);


    // const handleSearch = async (query) => {
    //     if (query) {
    //         try {
    //             const response = await fetch(`http://localhost:8000/api/petitions/search?query=${query}`, {
    //                 method: 'GET',
    //                 headers: {
    //                     'Content-Type': 'application/json',
    //                 },
    //             });
    //             if (!response.ok) {
    //                 throw new Error('네트워크 응답이 좋지 않습니다.');
    //             }
    //             const data = await response.json();
    //             setPetitions(data); // 검색된 청원 내용 설정
    //         } catch (error) {
    //             console.error('Error fetching petitions:', error);
    //             // 에러 처리 로직 추가 가능
    //         }
    //     } else {
    //         setPetitions([]); // 검색어가 없을 경우 초기화
    //     }
    // };

    return (
        <div className="space-y-8 p-4">
            {/*<SearchBar onSearch={handleSearch} />*/}
            <TopPetitions petitions={petitions} />
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <ExpiringPetitions petitions={petitions} />
                <CategoryPetitions category="환경" petitions={petitions} /> {/* 예시 카테고리 */}
            </div>
        </div>
    );
};

export default HomePage;