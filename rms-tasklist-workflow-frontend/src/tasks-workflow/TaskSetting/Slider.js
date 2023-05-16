import React from 'react';

const Switch = ({ isOn, handleToggle }) => {
    return (
        <>
            <label style={{
                display: isOn && 'none' || 'inline', fontWeight: 700, marginRight: 10,
                position: "relative", bottom: 15
            }}>
                Inactive
            </label>
            <label style={{
                display: isOn && 'inline' || 'none', fontWeight: 700, marginRight: 10,
                position: "relative", bottom: 15,
            }}>
                Active
            </label>
            <input
                checked={isOn}
                onChange={handleToggle}
                className="react-switch-checkbox"
                id={`react-switch-new`}
                type="checkbox"
            />
            <label style={{ background: isOn && '#88BC4A' }}
                className="react-switch-label"
                htmlFor={`react-switch-new`}
            >
                <span className={`react-switch-button`} />
            </label>
        </>
    );
};

export default Switch;