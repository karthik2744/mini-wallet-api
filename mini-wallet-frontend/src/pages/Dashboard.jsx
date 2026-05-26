import {
    useEffect,
    useState
} from "react";

import {
    useNavigate
} from "react-router-dom";

import api from "../api/axios";

function Dashboard() {

    const navigate = useNavigate();

    const [wallet, setWallet] =
        useState({});

    const [transactions,
        setTransactions] =
        useState([]);

    const [user,
        setUser] =
        useState({});

    const [showLogoutPopup,
        setShowLogoutPopup] =
        useState(false);

    // ACTIVE CHECK
    const isUserActive =

        user.active ??
        user.isActive ??
        false;

    // FETCH USER
    const fetchUser = async () => {

        try {

            const response =
                await api.get(
                    "/auth/me"
                );

            console.log(
                "USER:",
                response.data
            );

            setUser(
                response.data
            );

        } catch (err) {

            console.log(err);
        }
    };

    // FETCH WALLET
    const fetchWallet = async () => {

        try {

            const response =
                await api.get(
                    "/api/wallets/balance"
                );

            console.log(
                "WALLET:",
                response.data
            );

            setWallet(
                response.data
            );

        } catch (err) {

            console.log(err);
        }
    };

    // FETCH TRANSACTIONS
    const fetchTransactions =
        async () => {

            try {

                const response =
                    await api.get(
                        "/api/wallets/transactions"
                    );

                console.log(
                    "TRANSACTIONS:",
                    response.data
                );

                setTransactions(

                    response.data
                        .slice(0, 5)
                );

            } catch (err) {

                console.log(err);
            }
        };

    useEffect(() => {

        fetchUser();

        fetchWallet();

        fetchTransactions();

    }, []);

    return (

        <div className="min-h-screen bg-slate-950 text-white p-8">

            {/* HEADER */}

            <div className="flex justify-between items-start mb-10">

                <div>

                    <h1 className="text-4xl font-bold">

                        Welcome,
                        {" "}
                        {
                            user.name
                            ||
                            "User"
                        }

                    </h1>

                    <p className="text-slate-400 mt-2">

                        {
                            user.mobileNumber
                            ||
                            user.msisdn
                            ||
                            "No Mobile Number"
                        }

                    </p>

                    {/* STATUS */}

                    <div className="mt-4">

                        <span className={`px-4 py-2 rounded-full text-sm font-semibold ${

                            isUserActive

                                ? "bg-emerald-500/20 text-emerald-400"

                                : "bg-red-500/20 text-red-400"

                        }`}>

                            {
                                isUserActive

                                    ? "ACTIVE"

                                    : "INACTIVE"
                            }

                        </span>

                    </div>

                </div>

                {/* LOGOUT */}

                <button

                    onClick={() =>
                        setShowLogoutPopup(true)
                    }

                    className="bg-red-500 hover:bg-red-600 px-6 py-3 rounded-2xl font-semibold"
                >

                    Logout

                </button>

            </div>

            {/* BALANCE CARD */}

            <div className="bg-slate-900 p-8 rounded-3xl mb-10 border border-slate-800">

                <p className="text-slate-400 text-lg">

                    Available Balance

                </p>

                <h1 className="text-5xl font-bold text-emerald-400 mt-4">

                    ₹ {

                        wallet.balance
                        ||
                        wallet.availableBalance
                        ||
                        wallet.walletBalance
                        ||
                        0
                    }

                </h1>

                <div className="mt-6">

                    <p className="text-slate-400">

                        Wallet ID

                    </p>

                    <p className="mt-1">

                        {
                            wallet.id
                            ||
                            wallet.walletId
                            ||
                            wallet.wallet_id
                            ||
                            "N/A"
                        }

                    </p>

                </div>

            </div>

            {/* ACTION BUTTONS */}

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">

                {/* DEPOSIT */}

                <button

                    disabled={!isUserActive}

                    onClick={() =>
                        navigate("/deposit")
                    }

                    className={`p-6 rounded-3xl text-left transition-all ${

                        isUserActive

                            ? "bg-emerald-500 hover:bg-emerald-600"

                            : "bg-slate-800 cursor-not-allowed opacity-60"
                    }`}
                >

                    <h2 className="text-2xl font-bold">

                        Deposit

                    </h2>

                    <p className="mt-2">

                        Add money to wallet

                    </p>

                </button>

                {/* WITHDRAW */}

                <button

                    disabled={!isUserActive}

                    onClick={() =>
                        navigate("/withdraw")
                    }

                    className={`p-6 rounded-3xl text-left transition-all ${

                        isUserActive

                            ? "bg-cyan-500 hover:bg-cyan-600"

                            : "bg-slate-800 cursor-not-allowed opacity-60"
                    }`}
                >

                    <h2 className="text-2xl font-bold">

                        Withdraw

                    </h2>

                    <p className="mt-2">

                        Withdraw wallet balance

                    </p>

                </button>

                {/* TRANSACTIONS */}

                <button

                    onClick={() =>
                        navigate("/transactions")
                    }

                    className="bg-slate-900 hover:bg-slate-800 p-6 rounded-3xl text-left border border-slate-800"
                >

                    <h2 className="text-2xl font-bold">

                        Transactions

                    </h2>

                    <p className="mt-2 text-slate-400">

                        View transaction history

                    </p>

                </button>

            </div>

            {/* INACTIVE MESSAGE */}

            {
                !isUserActive && (

                    <div className="bg-red-500/20 border border-red-500 text-red-400 p-5 rounded-3xl mb-10">

                        Your account is inactive.
                        You can still view wallet details and transactions,
                        but deposits and withdrawals are disabled.

                    </div>
                )
            }

            {/* RECENT TRANSACTIONS */}

            <div className="bg-slate-900 p-8 rounded-3xl border border-slate-800">

                <div className="flex items-center justify-between mb-8">

                    <h2 className="text-3xl font-bold">

                        Recent Transactions

                    </h2>

                    <button

                        onClick={() =>
                            navigate("/transactions")
                        }

                        className="bg-emerald-500 hover:bg-emerald-600 px-5 py-3 rounded-2xl"
                    >

                        View All

                    </button>

                </div>

                <div className="space-y-4">

                    {
                        transactions.length === 0 && (

                            <p className="text-slate-400">

                                No transactions found

                            </p>
                        )
                    }

                    {
                        transactions.map((transaction) => {

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

                                            {
                                                transaction.referenceId
                                            }

                                        </p>

                                        <p className="text-slate-400 text-sm mt-1">

                                            {
                                                transaction.createdAt
                                            }

                                        </p>

                                    </div>

                                    <div className="text-right">

                                        <p className={`font-bold text-lg ${

                                            transaction.status === "FAILED"

                                                ? "text-red-400"

                                                : txnType === "CREDIT"

                                                    ? "text-emerald-400"

                                                    : "text-cyan-400"

                                        }`}>

                                            ₹ {
                                                transaction.amount
                                            }

                                        </p>

                                        <p className="text-sm text-slate-400 mt-1">

                                            {
                                                txnType
                                            }

                                            {" • "}

                                            {
                                                transaction.status
                                            }

                                        </p>

                                    </div>

                                </div>
                            );
                        })
                    }

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

export default Dashboard;