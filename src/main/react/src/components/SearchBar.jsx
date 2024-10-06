import React, { useState } from 'react';
import { Search } from 'lucide-react';

const SearchBar = ({ onSearch }) => {
    const [searchTerm, setSearchTerm] = useState('');


    const handleSearch = () => {
        onSearch(searchTerm);
    };

    return (
        <div className="relative mb-4">
            <input
                type="text"
                placeholder="청원 검색..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                        handleSearch();
                    }
                }}
                className="w-full p-4 pr-12 rounded-full border border-gray-300 bg-white text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400"
            />
            <Search className="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-500" onClick={handleSearch} />
        </div>
    );
};

export default SearchBar;