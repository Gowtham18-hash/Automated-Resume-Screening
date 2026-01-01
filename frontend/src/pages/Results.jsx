import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function Results() {
    const [resumes, setResumes] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchResumes();
    }, []);

    const fetchResumes = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/resumes/results');
            if (response.ok) {
                const data = await response.json();
                setResumes(data);
            }
        } catch (error) {
            console.error('Error fetching resumes:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleShortlist = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/resumes/shortlist/${id}`, {
                method: 'POST',
            });
            if (response.ok) {
                setResumes(resumes.filter(r => r.id !== id));
            }
        } catch (error) {
            console.error('Error shortlisting resume:', error);
        }
    };

    if (loading) return <div className="text-center">Loading results...</div>;

    return (
        <div style={{ maxWidth: '800px', margin: '0 auto' }}>
            <div className="mb-4">
                <h2 style={{ fontSize: '1.5rem' }}>Analysis Results</h2>
                <p className="text-muted">Matching Candidates Found</p>
            </div>

            {resumes.map(resume => (
                <div key={resume.id} className="glass-card mb-4">
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1.5rem' }}>
                        <h2 style={{ margin: 0, fontSize: '1.25rem' }}>{resume.filename}</h2>
                        <span className="badge-modern badge-success">
                            SCORE: {resume.score}%
                        </span>
                    </div>

                    <div className="mb-4">
                        <p className="text-muted small fw-bold mb-1">IDENTIFIED SKILLS</p>
                        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem' }}>
                            <span className="highlight-box">{resume.skills}</span>
                        </div>
                    </div>

                    <div style={{ borderTop: '1px solid var(--border-color)', paddingTop: '1.5rem' }}>
                        <div style={{ display: 'flex', gap: '1rem' }}>
                            <button
                                onClick={() => handleShortlist(resume.id)}
                                className="btn-primary"
                                style={{ flexGrow: 1 }}
                            >
                                Shortlist
                            </button>
                            <Link to={`/resume-details/${resume.id}`} className="btn-secondary">Details</Link>
                        </div>
                    </div>
                </div>
            ))}

            {resumes.length === 0 && (
                <div className="glass-card text-center py-5">
                    <p className="text-muted">No pending resumes found.</p>
                    <Link to="/" className="btn-primary mt-3">Upload New</Link>
                </div>
            )}
        </div>
    );
}

export default Results;
