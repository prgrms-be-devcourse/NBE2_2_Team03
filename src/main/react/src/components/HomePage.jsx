import React from 'react';
import SearchBar from './SearchBar.jsx';
import TopPetitions from './TopPetitions.jsx';
import ExpiringPetitions from './ExpiringPetitions.jsx';
import CategoryPetitions from './CategoryPetitions.jsx';

const HomePage = () => {
    return (
        <div className="space-y-8 p-4"> {/* 여백 추가 */}
            <SearchBar />
            <TopPetitions />
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <ExpiringPetitions />
                <CategoryPetitions />
            </div>
        </div>
    );
};

export default HomePage;
