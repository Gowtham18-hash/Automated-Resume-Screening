import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';

function ResumeDetails() {
    const { id } = useParams();
    const [resume, setResume] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchDetails = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/resumes/${id}`);
                if (response.ok) {
                    const data = await response.json();
                    setResume(data);
                }
            } catch (error) {
                console.error('Error fetching resume details:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchDetails();
    }, [id]);

    if (loading) return <div className="text-center">Loading details...</div>;
    if (!resume) return <div className="text-center">Resume not found</div>;

    return (
        <div>
            <header className="text-center">
                <h1 style={{ borderBottom: 'none' }}>{resume.filename}</h1>
                <p>Full Content Analysis</p>
            </header>

            <div className="glass-card mb-5">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '2px solid var(--text-color)', paddingBottom: '1rem', marginBottom: '2rem' }}>
                    <h2 style={{ margin: 0, fontSize: '1.5rem' }}>EXTRACTED TEXT</h2>
                    <span className="badge-modern badge-success">
                        MATCH SCORE: {resume.score}%
                    </span>
                </div>

                <div style={{
                    background: 'var(--ash-light)',
                    color: 'var(--text-color)',
                    padding: '2rem',
                    borderRadius: '4px',
                    border: '1px solid var(--border-color)',
                    whiteSpace: 'pre-wrap',
                    fontFamily: "'Courier New', Courier, monospace",
                    lineHeight: '1.6',
                    maxHeight: '500px',
                    overflowY: 'auto'
                }}>
                    <p className="mb-0">{resume.content}</p>
                </div>

                <div style={{ marginTop: '3rem', display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '2rem' }}>
                    <div className="highlight-box" style={{ padding: '1rem' }}>
                        <p className="text-muted small fw-bold mb-1">MATCHED SKILLS</p>
                        <p className="mb-0">{resume.skills}</p>
                    </div>
                    <div className="highlight-box" style={{ padding: '1rem' }}>
                        <p className="text-muted small fw-bold mb-1">ACADEMIC METRIC / CGPA</p>
                        <p className="mb-0">{resume.cgpa}</p>
                    </div>
                </div>
            </div>

            <div className="text-center mt-5">
                <Link to="/shortlist" className="btn-secondary">Back to Shortlist</Link>
            </div>
        </div>
    );
}

export default ResumeDetails;
