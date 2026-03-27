import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { login as loginApi } from '../../api/auth';
import { useAuth } from '../../context/AuthContext';
import { parseErrorMessage } from '../../utils/formatters';

export default function LoginPage() {
    const navigate = useNavigate();
    const { login } = useAuth();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            const res = await loginApi(email, password);
            login(res.data.token);
            navigate('/home');
        } catch (err) {
            setError(parseErrorMessage(err) || 'Invalid email or password');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-background flex flex-col items-center justify-center px-4">
            <div className="w-full max-w-sm">
                {/* Logo */}
                <div className="text-center mb-10">
                    <div className="w-16 h-16 rounded-2xl bg-primary flex items-center justify-center mx-auto mb-4 shadow-lg">
                        <span className="text-white text-2xl font-bold">FF</span>
                    </div>
                    <h1 className="text-3xl font-bold text-text-primary">FormFit</h1>
                    <p className="text-text-secondary text-sm mt-1">Track every rep. Break every record.</p>
                </div>

                <form onSubmit={handleSubmit} className="flex flex-col gap-3">
                    <input
                        className="input-field"
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                        required
                    />
                    <input
                        className="input-field"
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        required
                    />
                    {error && <p className="text-danger text-sm text-center">{error}</p>}
                    <button type="submit" className="btn-primary mt-2" disabled={loading}>
                        {loading ? 'Logging in…' : 'Login'}
                    </button>
                </form>

                <p className="text-center text-sm text-text-secondary mt-6">
                    Don't have an account?{' '}
                    <Link to="/register" className="text-primary font-semibold">Register</Link>
                </p>
            </div>
        </div>
    );
}
