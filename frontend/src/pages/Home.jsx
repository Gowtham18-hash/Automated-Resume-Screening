import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Home() {
    const [files, setFiles] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleFileChange = (e) => {
        setFiles(e.target.files);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!files || files.length === 0) return;

        setLoading(true);
        const formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }

        try {
            const response = await fetch('http://localhost:8080/api/resumes/upload', {
                method: 'POST',
                body: formData,
            });

            if (response.ok) {
                navigate('/results');
            } else {
                alert('Upload failed');
            }
        } catch (error) {
            console.error('Error uploading files:', error);
            alert('Error uploading files');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <div className="text-center mb-5">
                <p className="text-muted">Automated Resume Matching Engine</p>
            </div>

            <div className="glass-card mx-auto" style={{ maxWidth: '600px' }}>
                <h2>Analyze Resumes</h2>
                <p className="text-muted mb-4">Upload PDF or DOCX files to initiate AI-based ranking.</p>

                <form onSubmit={handleSubmit}>
                    <input
                        type="file"
                        multiple
                        required
                        className="form-control-modern mb-4"
                        onChange={handleFileChange}
                    />
                    <button type="submit" className="btn-primary w-100" disabled={loading}>
                        {loading ? 'Analyzing...' : 'Start Analysis'}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Home;
