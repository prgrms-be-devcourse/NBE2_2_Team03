import React, { useState, useEffect } from 'react';
import SearchBar from './SearchBar.jsx';
import TopPetitions from './TopPetitions.jsx';
import ExpiringPetitions from './ExpiringPetitions.jsx';
import CategoryPetitions from './CategoryPetitions.jsx';
import axios from 'axios';

const HomePage = () => {
    const [petitions, setPetitions] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');

    // 청원 목록을 가져오는 함수
    const fetchPetitions = async () => {
        try {
            const response = await axios.get('http://localhost:8000/api/petitions');
            setPetitions(response.data);
        } catch (error) {
            console.error('Error fetching petitions:', error);
        }
    };

    // 검색 기능
    const handleSearch = async (query) => {
        setSearchQuery(query);
        if (query) {
            try {
                const response = await axios.get(`http://localhost:8000/api/petitions/search?query=${query}`);
                setPetitions(response.data); // 검색된 청원 내용 설정
            } catch (error) {
                console.error('Error fetching petitions:', error);
            }
        } else {
            fetchPetitions(); // 검색어가 없을 경우 전체 청원 가져오기
        }
    };

    useEffect(() => {
        fetchPetitions(); // 컴포넌트가 마운트될 때 청원 목록 가져오기
    }, []);

    return (
        <div className="space-y-8 p-4">
            <SearchBar onSearch={handleSearch} />
            <TopPetitions petitions={petitions} />
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <ExpiringPetitions petitions={petitions} />
                <CategoryPetitions category="환경" petitions={petitions} /> {/* 예시 카테고리 */}
            </div>
        </div>
    );
};

export default HomePage;
