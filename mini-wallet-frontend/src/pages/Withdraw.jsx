import { useEffect, useState }
from "react";

import { useNavigate }
from "react-router-dom";

import api from "../api/axios";

function Withdraw() {

    const navigate = useNavigate();

    const [amount, setAmount] =
        useState("");

    const [wallet, setWallet] =
        useState({});

    const [user, setUser] =
        useState({});

    const [showConfirm, setShowConfirm] =
        useState(false);

    const [success, setSuccess] =
        useState(false);

    const [error, setError] =
        useState("");

    // FETCH USER + WALLET DETAILS
    const fetchData = async () => {

        try {

            const userResponse =
                await api.get(
                    "/auth/me"
                );

            setUser(
                userResponse.data
            );

            const walletResponse =
                await api.get(
                    "/api/wallets/balance"
                );

            setWallet(
                walletResponse.data
            );

        } catch (err) {

            console.log(err);
        }
    };

    useEffect(() => {

        fetchData();

    }, []);

    // HANDLE WITHDRAW
    const handleWithdraw =
        async () => {

            try {

                setError("");

                await api.post(
                    "/api/wallets/withdraw",
                    {
                        amount
                    }
                );

                setSuccess(true);

                setTimeout(() => {

                    navigate("/dashboard");

                }, 2000);

            } catch (err) {

                setShowConfirm(false);

                setError(

                    err.response?.data?.message ||

                    err.response?.data?.error ||

                    "Transaction failed"
                );
            }
        };

    return (

        <div className="min-h-screen bg-gradient-to-br from-slate-950 via-slate-900 to-cyan-950 text-white">

            {/* NAVBAR */}

            <div className="flex items-center justify-between p-6 border-b border-slate-800">

                <h1 className="text-3xl font-bold">

                    Withdraw Money

                </h1>

                <button
                    onClick={() =>
                        navigate("/dashboard")
                    }
                    className="bg-slate-700 hover:bg-slate-600 px-5 py-2 rounded-xl"
                >

                    Back

                </button>

            </div>

            {/* MAIN */}

            <div className="flex items-center justify-center p-10">

                <div className="bg-slate-900/70 border border-slate-700 rounded-3xl p-10 w-full max-w-xl shadow-2xl">

                    {/* USER DETAILS */}

                    <div className="mb-8">

                        <h2 className="text-2xl font-bold mb-5">

                            Wallet Details

                        </h2>

                        <div className="space-y-3 text-slate-300">

                            <p>

                                <span className="text-slate-500">

                                    Name:

                                </span>

                                {" "}
                                {user.name}

                            </p>

                            <p>

                                <span className="text-slate-500">

                                    Mobile:

                                </span>

                                {" "}
                                {user.mobileNumber}

                            </p>

                            <p>

                                <span className="text-slate-500">

                                    Wallet ID:

                                </span>

                                {" "}
                                {wallet.id}

                            </p>

                            <p className="text-4xl font-bold text-cyan-400 mt-5">

                                ₹ {wallet.balance}

                            </p>

                        </div>

                    </div>

                    {/* ERROR */}

                    {error && (

                        <div className="bg-red-500/20 text-red-400 p-4 rounded-xl mb-5">

                            {error}

                        </div>
                    )}

                    {/* AMOUNT */}

                    <div>

                        <label className="block mb-3 text-slate-300">

                            Enter Amount

                        </label>

                        <input
                            type="number"
                            placeholder="Enter withdraw amount"
                            value={amount}
                            onChange={(e) =>
                                setAmount(
                                    e.target.value
                                )
                            }
                            className="w-full p-4 rounded-xl bg-slate-800 border border-slate-700 mb-6"
                        />

                        <button
                            onClick={() =>
                                setShowConfirm(true)
                            }
                            className="w-full bg-cyan-500 hover:bg-cyan-600 p-4 rounded-xl font-semibold"
                        >

                            Continue

                        </button>

                    </div>

                </div>

            </div>

            {/* CONFIRM MODAL */}

            {showConfirm && (

                <div className="fixed inset-0 bg-black/70 flex items-center justify-center">

                    <div className="bg-slate-900 p-8 rounded-3xl w-[400px] border border-slate-700">

                        <h2 className="text-2xl font-bold mb-5">

                            Confirm Withdraw

                        </h2>

                        <div className="space-y-3 text-slate-300 mb-6">

                            <p>

                                Name:
                                {" "}
                                {user.name}

                            </p>

                            <p>

                                Mobile:
                                {" "}
                                {user.mobileNumber}

                            </p>

                            <p>

                                Amount:
                                {" "}

                                <span className="text-cyan-400 font-bold">

                                    ₹ {amount}

                                </span>

                            </p>

                        </div>

                        <div className="flex gap-4">

                            <button
                                onClick={handleWithdraw}
                                className="flex-1 bg-cyan-500 hover:bg-cyan-600 p-3 rounded-xl"
                            >

                                Confirm

                            </button>

                            <button
                                onClick={() =>
                                    setShowConfirm(false)
                                }
                                className="flex-1 bg-slate-700 hover:bg-slate-600 p-3 rounded-xl"
                            >

                                Cancel

                            </button>

                        </div>

                    </div>

                </div>
            )}

            {/* SUCCESS MESSAGE */}

            {success && (

                <div className="fixed top-10 right-10 bg-emerald-500 text-white px-6 py-4 rounded-2xl shadow-2xl">

                    Transaction Successful ✅

                </div>
            )}

        </div>
    );
}

export default Withdraw;