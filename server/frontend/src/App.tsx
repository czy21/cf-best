import React from 'react';
import Home from "@/layout/Home";
import {BrowserRouter} from "react-router-dom";

// const store = createStore(rootReducer);

const App: React.FC = () => {
    return (
        <BrowserRouter basename={process.env.REACT_APP_BASE_URL}>
            <Home/>
        </BrowserRouter>
    )
}

export default App;
