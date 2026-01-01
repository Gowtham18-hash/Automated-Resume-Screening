import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, NavLink } from 'react-router-dom';
import Home from './pages/Home';
import Results from './pages/Results';
import Shortlist from './pages/Shortlist';
import ResumeDetails from './pages/ResumeDetails';

function App() {
  return (
    <Router>
      <div className="container">
        <header className="site-header">
          <Link to="/" className="logo">spring.ai</Link>
          <nav className="navbar">
            <NavLink to="/" end className={({ isActive }) => isActive ? 'active' : ''}>UPLOAD</NavLink>
            <NavLink to="/results" className={({ isActive }) => isActive ? 'active' : ''}>RESULTS</NavLink>
            <NavLink to="/shortlist" className={({ isActive }) => isActive ? 'active' : ''}>SHORTLIST</NavLink>
          </nav>
        </header>

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/results" element={<Results />} />
          <Route path="/shortlist" element={<Shortlist />} />
          <Route path="/resume-details/:id" element={<ResumeDetails />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
