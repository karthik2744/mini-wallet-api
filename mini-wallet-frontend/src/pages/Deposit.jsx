import { useEffect, useState }
from "react";

import { useNavigate }
from "react-router-dom";

import api from "../api/axios";

function Deposit() {

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

    // FETCH USER DETAILS
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

    // HANDLE DEPOSIT
    const handleDeposit =
        async () => {

            try {

                await api.post(
                    "/api/wallets/deposit",
                    {
                        amount
                    }
                );

                setSuccess(true);

                setTimeout(() => {

                    navigate("/dashboard");

                }, 2000);

            } catch (err) {

               alert(
                   err.response?.data?.message ||
                   err.response?.data?.error ||
                   "Transaction failed"
               );
            }
        };

    return (

        <div className="min-h-screen bg-gradient-to-br from-slate-950 via-slate-900 to-emerald-950 text-white">

            {/* NAVBAR */}

            <div className="flex items-center justify-between p-6 border-b border-slate-800">

                <h1 className="text-3xl font-bold">

                    Deposit Money

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

                            <p className="text-4xl font-bold text-emerald-400 mt-5">

                                ₹ {wallet.balance}

                            </p>

                        </div>

                    </div>

                    {/* AMOUNT */}

                    <div>

                        <label className="block mb-3 text-slate-300">

                            Enter Amount

                        </label>

                        <input
                            type="number"
                            placeholder="Enter deposit amount"
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
                            className="w-full bg-emerald-500 hover:bg-emerald-600 p-4 rounded-xl font-semibold"
                        >

                            Continue

                        </button>

                    </div>

                </div>

            </div>

            {/* CONFIRM POPUP */}

            {showConfirm && (

                <div className="fixed inset-0 bg-black/70 flex items-center justify-center">

                    <div className="bg-slate-900 p-8 rounded-3xl w-[400px] border border-slate-700">

                        <h2 className="text-2xl font-bold mb-5">

                            Confirm Deposit

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
                                <span className="text-emerald-400 font-bold">

                                    ₹ {amount}

                                </span>

                            </p>

                        </div>

                        <div className="flex gap-4">

                            <button
                                onClick={handleDeposit}
                                className="flex-1 bg-emerald-500 hover:bg-emerald-600 p-3 rounded-xl"
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

export default Deposit;