import React from 'react';
import SearchBar from './SearchBar';
import TopPetitions from './TopPetitions';
import ExpiringPetitions from './ExpiringPetitions';
import CategoryPetitions from './CategoryPetitions';

const HomePage = () => {
    return (
        <div className="space-y-8">
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
