import {
    useEffect,
    useState
} from "react";

import {
    useNavigate
} from "react-router-dom";

import api from "../api/axios";

import {

    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
    CartesianGrid,

    PieChart,
    Pie,
    Cell,
    Legend

} from "recharts";

function AdminDashboard() {

    const navigate = useNavigate();

    const [dashboard, setDashboard] =
        useState({});

    const [analytics, setAnalytics] =
        useState([]);

    const [recentTransactions,
        setRecentTransactions] =
        useState([]);

    const [showLogoutPopup,
        setShowLogoutPopup] =
        useState(false);

    const COLORS = [

        "#10b981",

        "#ef4444"
    ];

    const TYPE_COLORS = [

        "#10b981",

        "#06b6d4"
    ];

    const fetchData = async () => {

        try {

            // DASHBOARD

            const dashboardResponse =
                await api.get(
                    "/api/admin/dashboard"
                );

            setDashboard(
                dashboardResponse.data
            );

            // ANALYTICS

            const analyticsResponse =
                await api.get(
                    "/api/admin/analytics"
                );

            setAnalytics(
                analyticsResponse.data
            );

            // RECENT TRANSACTIONS

            const transactionResponse =
                await api.get(
                    "/api/admin/transactions"
                );

            setRecentTransactions(

                transactionResponse.data
                    .slice(0, 5)
            );

        } catch (err) {

            console.log(err);
        }
    };

    useEffect(() => {

        fetchData();

    }, []);

    // STATUS PIE

    const pieData = [

        {
            name: "Success",

            value:
                dashboard.totalTransactions
                -
                dashboard.failedTransactions
        },

        {
            name: "Failed",

            value:
                dashboard.failedTransactions
        }
    ];

    // TYPE PIE

    const transactionTypeData = [

        {
            name: "Credit",

            value:
                dashboard.creditTransactions
        },

        {
            name: "Debit",

            value:
                dashboard.debitTransactions
        }
    ];

    return (

        <div className="flex min-h-screen bg-slate-950 text-white">

            {/* SIDEBAR */}

            <div className="w-72 bg-slate-900 border-r border-slate-800 p-6 flex flex-col">

                <h1 className="text-3xl font-bold mb-12">

                    Mini Wallet

                </h1>

                <div className="space-y-4">

                    <button
                        className="w-full bg-emerald-500 text-white p-4 rounded-2xl text-left"
                    >

                        Dashboard

                    </button>

                    <button
                        onClick={() =>
                            navigate("/admin/users")
                        }
                        className="w-full bg-slate-800 hover:bg-slate-700 p-4 rounded-2xl text-left"
                    >

                        Users

                    </button>

                    <button
                        onClick={() =>
                            navigate("/admin/transactions")
                        }
                        className="w-full bg-slate-800 hover:bg-slate-700 p-4 rounded-2xl text-left"
                    >

                        Transactions

                    </button>

                </div>

                <div className="mt-auto">

                    <button

                        onClick={() =>
                            setShowLogoutPopup(true)
                        }

                        className="bg-red-500 hover:bg-red-600 px-6 py-3 rounded-2xl w-full"
                    >

                        Logout

                    </button>

                </div>

            </div>

            {/* MAIN */}

            <div className="flex-1 p-10">

                <h1 className="text-4xl font-bold mb-10">

                    Admin Dashboard

                </h1>

                {/* CARDS */}

                <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-10">

                    <div className="bg-slate-900 p-6 rounded-3xl">

                        <p className="text-slate-400">

                            Total Users

                        </p>

                        <h1 className="text-4xl font-bold mt-4">

                            {dashboard.totalUsers}

                        </h1>

                    </div>

                    <div className="bg-slate-900 p-6 rounded-3xl">

                        <p className="text-slate-400">

                            Wallet Balance

                        </p>

                        <h1 className="text-4xl font-bold mt-4 text-emerald-400">

                            ₹ {dashboard.totalWalletBalance}

                        </h1>

                    </div>

                    <div className="bg-slate-900 p-6 rounded-3xl">

                        <p className="text-slate-400">

                            Transactions

                        </p>

                        <h1 className="text-4xl font-bold mt-4">

                            {dashboard.totalTransactions}

                        </h1>

                    </div>

                    <div className="bg-slate-900 p-6 rounded-3xl">

                        <p className="text-slate-400">

                            Failed Transactions

                        </p>

                        <h1 className="text-4xl font-bold mt-4 text-red-400">

                            {dashboard.failedTransactions}

                        </h1>

                    </div>

                </div>

                {/* CHARTS */}

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 mb-10">

                    {/* LINE CHART */}

                    <div className="bg-slate-900 p-8 rounded-3xl lg:col-span-1">

                        <h2 className="text-2xl font-bold mb-6">

                            Daily Transaction Volume

                        </h2>

                        <ResponsiveContainer
                            width="100%"
                            height={300}
                        >

                            <LineChart data={analytics}>

                                <CartesianGrid strokeDasharray="3 3" />

                                <XAxis dataKey="date" />

                                <YAxis />

                                <Tooltip />

                                <Line
                                    type="monotone"
                                    dataKey="transactionCount"
                                    stroke="#10b981"
                                />

                            </LineChart>

                        </ResponsiveContainer>

                    </div>

                    {/* STATUS PIE */}

                    <div className="bg-slate-900 p-8 rounded-3xl">

                        <h2 className="text-2xl font-bold mb-6">

                            Transaction Status

                        </h2>

                        <ResponsiveContainer
                            width="100%"
                            height={300}
                        >

                            <PieChart>

                                <Pie
                                    data={pieData}
                                    cx="50%"
                                    cy="50%"
                                    outerRadius={90}
                                    dataKey="value"
                                    label
                                >

                                    {
                                        pieData.map(
                                            (entry, index) => (

                                                <Cell
                                                    key={index}
                                                    fill={
                                                        COLORS[index]
                                                    }
                                                />
                                            )
                                        )
                                    }

                                </Pie>

                                <Tooltip />

                                <Legend />

                            </PieChart>

                        </ResponsiveContainer>

                    </div>

                    {/* TYPE PIE */}

                    <div className="bg-slate-900 p-8 rounded-3xl">

                        <h2 className="text-2xl font-bold mb-6">

                            Transaction Types

                        </h2>

                        <ResponsiveContainer
                            width="100%"
                            height={300}
                        >

                            <PieChart>

                                <Pie
                                    data={transactionTypeData}
                                    cx="50%"
                                    cy="50%"
                                    outerRadius={90}
                                    dataKey="value"
                                    label
                                >

                                    {
                                        transactionTypeData.map(
                                            (entry, index) => (

                                                <Cell
                                                    key={index}
                                                    fill={
                                                        TYPE_COLORS[index]
                                                    }
                                                />
                                            )
                                        )
                                    }

                                </Pie>

                                <Tooltip />

                                <Legend />

                            </PieChart>

                        </ResponsiveContainer>

                    </div>

                </div>

                {/* RECENT TRANSACTIONS */}

                <div className="bg-slate-900 p-8 rounded-3xl">

                    <div className="flex items-center justify-between mb-6">

                        <h2 className="text-2xl font-bold">

                            Recent Transactions

                        </h2>

                        <button
                            onClick={() =>
                                navigate(
                                    "/admin/transactions"
                                )
                            }
                            className="bg-emerald-500 hover:bg-emerald-600 px-5 py-2 rounded-xl"
                        >

                            View All

                        </button>

                    </div>

                    <div className="space-y-4">

                        {
                            recentTransactions.length === 0 && (

                                <p className="text-slate-400">

                                    No transactions found

                                </p>
                            )
                        }

                        {
                            recentTransactions.map((transaction) => {

                                const txnType =

                                    transaction.type
                                    ||
                                    transaction.transactionType
                                    ||
                                    transaction.transaction_type;

                                return (

                                    <div
                                        key={transaction.referenceId}
                                        className="bg-slate-800 p-5 rounded-2xl flex justify-between items-center"
                                    >

                                        <div>

                                            <p className="font-semibold">

                                                {transaction.referenceId}

                                            </p>

                                            <p className="text-slate-400 text-sm">

                                                {
                                                    transaction.mobileNumber
                                                    ||
                                                    transaction.msisdn
                                                }

                                            </p>

                                        </div>

                                        <div className="text-right">

                                            <p className={`font-bold ${

                                                transaction.status === "FAILED"

                                                    ? "text-red-400"

                                                    : txnType === "CREDIT"

                                                        ? "text-emerald-400"

                                                        : "text-cyan-400"

                                            }`}>

                                                ₹ {transaction.amount}

                                            </p>

                                            <p className="text-sm text-slate-400">

                                                {txnType}

                                            </p>

                                        </div>

                                    </div>
                                );
                            })
                        }

                    </div>

                </div>

            </div>

            {/* LOGOUT POPUP */}

            {
                showLogoutPopup && (

                    <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50">

                        <div className="bg-slate-900 p-8 rounded-3xl w-[400px] border border-slate-700">

                            <h2 className="text-2xl font-bold mb-4">

                                Confirm Logout

                            </h2>

                            <p className="text-slate-400 mb-8">

                                Are you sure you want to logout?

                            </p>

                            <div className="flex justify-end gap-4">

                                <button

                                    onClick={() =>
                                        setShowLogoutPopup(false)
                                    }

                                    className="bg-slate-700 hover:bg-slate-600 px-5 py-3 rounded-2xl"
                                >

                                    Cancel

                                </button>

                                <button

                                    onClick={() => {

                                        localStorage.clear();

                                        navigate("/login");
                                    }}

                                    className="bg-red-500 hover:bg-red-600 px-5 py-3 rounded-2xl"
                                >

                                    Logout

                                </button>

                            </div>

                        </div>

                    </div>
                )
            }

        </div>
    );
}

export default AdminDashboard;