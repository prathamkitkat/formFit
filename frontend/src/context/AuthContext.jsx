import { createContext, useContext, useState, useEffect } from 'react';
import { getMe } from '../api/user';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [token, setToken] = useState(() => localStorage.getItem('token'));
    const [currentUser, setCurrentUser] = useState(null);
    const [weightUnit, setWeightUnit] = useState('KG');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (token) {
            getMe()
                .then(res => {
                    setCurrentUser(res.data);
                    if (res.data.weightUnit) setWeightUnit(res.data.weightUnit);
                })
                .catch(() => {
                    localStorage.removeItem('token');
                    setToken(null);
                })
                .finally(() => setLoading(false));
        } else {
            setLoading(false);
        }
    }, [token]);

    const login = (newToken) => {
        localStorage.setItem('token', newToken);
        setToken(newToken);
    };

    const logout = () => {
        localStorage.removeItem('token');
        setToken(null);
        setCurrentUser(null);
        window.location.href = '/login';
    };

    const updateWeightUnit = (unit) => setWeightUnit(unit);

    return (
        <AuthContext.Provider value={{ token, currentUser, weightUnit, loading, login, logout, updateWeightUnit }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}
