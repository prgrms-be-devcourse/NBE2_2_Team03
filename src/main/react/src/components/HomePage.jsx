import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom'; // useNavigate 추가
import SearchBar from './SearchBar.jsx';
import TopPetitions from './TopPetitions.jsx';
import ExpiringPetitions from './ExpiringPetitions.jsx';
import CategoryPetitions from './CategoryPetitions.jsx';
import axios from 'axios';

const HomePage = () => {
    const [petitions, setPetitions] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const navigate = useNavigate(); // useNavigate 훅 사용

    const fetchPetitions = async () => {
        try {
            const response = await axios.get('http://localhost:8000/api/petitions');
            setPetitions(response.data);
        } catch (error) {
            console.error('Error fetching petitions:', error);
        }
    };

    const handleSearch = (query) => {
        setSearchQuery(query);
        // 검색 결과 페이지로 이동 (쿼리 파라미터 없이 AllPetitionsPage로 이동)
        navigate('/petitions');
    };

    useEffect(() => {
        fetchPetitions();
    }, []);

    return (
        <div className="space-y-8 p-4">
            <SearchBar onSearch={handleSearch} />
            <TopPetitions petitions={petitions} />
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <ExpiringPetitions petitions={petitions} />
                <CategoryPetitions category="환경" petitions={petitions} />
            </div>
        </div>
    );
};

export default HomePage;
