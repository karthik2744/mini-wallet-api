import {
    BrowserRouter,
    Routes,
    Route
} from "react-router-dom";

import Login from "../pages/Login";

import AdminDashboard from "../pages/AdminDashboard";

import AdminUsers from "../pages/AdminUsers";

import AdminTransactions from "../pages/AdminTransactions";

import Register from "../pages/Register";

import Dashboard from "../pages/Dashboard";

import Deposit from "../pages/Deposit";

import Withdraw from "../pages/Withdraw";

import Transactions from "../pages/Transactions";



function AppRoutes() {

    return (

        <BrowserRouter>

            <Routes>

                <Route
                    path="/"
                    element={<Login />}
                />

                <Route
                    path="/login"
                    element={<Login />}
                />

                <Route
                    path="/register"
                    element={<Register />}
                />

                <Route
                    path="/dashboard"
                    element={<Dashboard />}
                />

                <Route
                    path="/deposit"
                    element={<Deposit />}
                />

                <Route
                    path="/withdraw"
                    element={<Withdraw />}
                />

                <Route
                    path="/transactions"
                    element={<Transactions />}
                />
                <Route
                    path="/admin-dashboard"
                    element={<AdminDashboard />}
                />

                <Route
                    path="/admin/users"
                    element={<AdminUsers />}
                />

                <Route
                    path="/admin/transactions"
                    element={<AdminTransactions />}
                />

            </Routes>

        </BrowserRouter>
    );
}

export default AppRoutes;