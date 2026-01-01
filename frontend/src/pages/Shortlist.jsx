import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function Shortlist() {
    const [resumes, setResumes] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchShortlisted();
    }, []);

    const fetchShortlisted = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/resumes/shortlisted');
            if (response.ok) {
                const data = await response.json();
                setResumes(data);
            }
        } catch (error) {
            console.error('Error fetching shortlisted resumes:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleRemove = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/resumes/unshortlist/${id}`, {
                method: 'POST',
            });
            if (response.ok) {
                setResumes(resumes.filter(r => r.id !== id));
            }
        } catch (error) {
            console.error('Error removing from shortlist:', error);
        }
    };

    if (loading) return <div className="text-center">Loading shortlist...</div>;

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto' }}>
            <header className="text-center mb-5">
                <h1 style={{ borderBottom: 'none', fontSize: '1.5rem', margin: 0 }}>Shortlisted Resumes</h1>
                <p className="text-muted">Selected Candidates for Review</p>
            </header>

            {resumes.length === 0 && (
                <div className="glass-card text-center py-5">
                    <p className="text-muted">No resumes shortlisted yet.</p>
                    <Link to="/results" className="btn-primary mt-3">View All Results</Link>
                </div>
            )}

            {resumes.map(resume => (
                <div key={resume.id} className="glass-card mb-4">
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '2rem' }}>
                        <h2 style={{ margin: 0 }}>{resume.filename}</h2>
                        <span className="badge-modern badge-success">
                            SCORE: {resume.score}%
                        </span>
                    </div>

                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem', marginBottom: '2rem' }}>
                        <div>
                            <p className="text-muted small fw-bold mb-1">IDENTIFIED SKILLS</p>
                            <p style={{ fontSize: '0.95rem' }}>{resume.skills}</p>
                        </div>
                        <div style={{ textAlign: 'right', display: 'flex', gap: '1rem', justifyContent: 'flex-end' }}>
                            <button onClick={() => handleRemove(resume.id)} className="btn-secondary">Remove</button>
                            <Link to={`/resume-details/${resume.id}`} className="btn-primary">Full Analysis</Link>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default Shortlist;
