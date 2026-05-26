import {
    useEffect,
    useState
} from "react";

import {
    useNavigate
} from "react-router-dom";

import api from "../api/axios";

function Transactions() {

    const navigate = useNavigate();

    const [transactions,
        setTransactions] =
        useState([]);

    const [filteredTransactions,
        setFilteredTransactions] =
        useState([]);

    const [loading,
        setLoading] =
        useState(true);

    const [typeFilter,
        setTypeFilter] =
        useState("");

    const [statusFilter,
        setStatusFilter] =
        useState("");

    // PAGINATION

    const [currentPage,
        setCurrentPage] =
        useState(1);

    const transactionsPerPage = 10;

    // FETCH TRANSACTIONS

    const fetchTransactions =
        async () => {

            try {

                const response =
                    await api.get(
                        "/api/wallets/transactions"
                    );

                console.log(
                    "Transactions:",
                    response.data
                );

                setTransactions(
                    response.data
                );

                setFilteredTransactions(
                    response.data
                );

            } catch (err) {

                console.log(err);

            } finally {

                setLoading(false);
            }
        };

    useEffect(() => {

        fetchTransactions();

    }, []);

    // FILTER LOGIC

    useEffect(() => {

        let filtered =
            [...transactions];

        if (typeFilter) {

            filtered =
                filtered.filter((t) => {

                    const txnType =

                        t.type
                        ||
                        t.transactionType
                        ||
                        t.transaction_type;

                    return txnType === typeFilter;
                });
        }

        if (statusFilter) {

            filtered =
                filtered.filter(

                    (t) =>

                        t.status === statusFilter
                );
        }

        setFilteredTransactions(
            filtered
        );

        setCurrentPage(1);

    }, [
        typeFilter,
        statusFilter,
        transactions
    ]);

    // PAGINATION LOGIC

    const indexOfLastTransaction =
        currentPage * transactionsPerPage;

    const indexOfFirstTransaction =
        indexOfLastTransaction
        - transactionsPerPage;

    const currentTransactions =

        filteredTransactions.slice(

            indexOfFirstTransaction,

            indexOfLastTransaction
        );

    const totalPages =

        Math.ceil(

            filteredTransactions.length
            /
            transactionsPerPage
        );

    return (

        <div className="min-h-screen bg-slate-950 text-white p-8">

            {/* HEADER */}

            <div className="flex items-center justify-between mb-10">

                <div>

                    <h1 className="text-4xl font-bold">

                        Transaction History

                    </h1>

                    <p className="text-slate-400 mt-2">

                        View all wallet transactions

                    </p>

                </div>

                <button

                    onClick={() =>
                        navigate("/dashboard")
                    }

                    className="bg-slate-800 hover:bg-slate-700 px-6 py-3 rounded-2xl"
                >

                    Back

                </button>

            </div>

            {/* FILTERS */}

            <div className="flex gap-4 mb-8">

                {/* TYPE FILTER */}

                <select

                    value={typeFilter}

                    onChange={(e) =>
                        setTypeFilter(
                            e.target.value
                        )
                    }

                    className="bg-slate-900 border border-slate-700 p-4 rounded-2xl"
                >

                    <option value="">
                        All Types
                    </option>

                    <option value="CREDIT">
                        CREDIT
                    </option>

                    <option value="DEBIT">
                        DEBIT
                    </option>

                </select>

                {/* STATUS FILTER */}

                <select

                    value={statusFilter}

                    onChange={(e) =>
                        setStatusFilter(
                            e.target.value
                        )
                    }

                    className="bg-slate-900 border border-slate-700 p-4 rounded-2xl"
                >

                    <option value="">
                        All Status
                    </option>

                    <option value="SUCCESS">
                        SUCCESS
                    </option>

                    <option value="FAILED">
                        FAILED
                    </option>

                </select>

            </div>

            {/* LOADING */}

            {
                loading && (

                    <div className="bg-slate-900 p-10 rounded-3xl text-center border border-slate-800">

                        Loading transactions...

                    </div>
                )
            }

            {/* EMPTY */}

            {
                !loading &&
                filteredTransactions.length === 0 && (

                    <div className="bg-slate-900 p-10 rounded-3xl text-center border border-slate-800">

                        <p className="text-slate-400">

                            No transactions found

                        </p>

                    </div>
                )
            }

            {/* TABLE */}

            {
                !loading &&
                filteredTransactions.length > 0 && (

                    <>

                        <div className="bg-slate-900 rounded-3xl overflow-hidden border border-slate-800">

                            <table className="w-full">

                                <thead className="bg-slate-800">

                                <tr>

                                    <th className="text-left p-5">

                                        Reference ID

                                    </th>

                                    <th className="text-left p-5">

                                        MSISDN

                                    </th>

                                    <th className="text-left p-5">

                                        Type

                                    </th>

                                    <th className="text-left p-5">

                                        Status

                                    </th>

                                    <th className="text-left p-5">

                                        Amount

                                    </th>

                                    <th className="text-left p-5">

                                        Balance

                                    </th>

                                    <th className="text-left p-5">

                                        Date

                                    </th>

                                </tr>

                                </thead>

                                <tbody>

                                {
                                    currentTransactions.map((transaction) => {

                                        const txnType =

                                            transaction.type
                                            ||
                                            transaction.transactionType
                                            ||
                                            transaction.transaction_type;

                                        return (

                                            <tr

                                                key={
                                                    transaction.referenceId
                                                }

                                                className="border-t border-slate-800"
                                            >

                                                {/* REF ID */}

                                                <td className="p-5">

                                                    {
                                                        transaction.referenceId
                                                    }

                                                </td>

                                                {/* MSISDN */}

                                                <td className="p-5">

                                                    {
                                                        transaction.mobileNumber
                                                        ||
                                                        transaction.msisdn
                                                        ||
                                                        "N/A"
                                                    }

                                                </td>

                                                {/* TYPE */}

                                                <td className="p-5">

                                                    <span className={`px-4 py-2 rounded-full text-sm font-semibold ${

                                                        txnType === "CREDIT"

                                                            ? "bg-emerald-500/20 text-emerald-400"

                                                            : "bg-cyan-500/20 text-cyan-400"

                                                    }`}>

                                                        {
                                                            txnType
                                                        }

                                                    </span>

                                                </td>

                                                {/* STATUS */}

                                                <td className="p-5">

                                                    <span className={`px-4 py-2 rounded-full text-sm font-semibold ${

                                                        transaction.status === "SUCCESS"

                                                            ? "bg-emerald-500/20 text-emerald-400"

                                                            : "bg-red-500/20 text-red-400"

                                                    }`}>

                                                        {
                                                            transaction.status
                                                        }

                                                    </span>

                                                </td>

                                                {/* AMOUNT */}

                                                <td className={`p-5 font-bold ${

                                                    transaction.status === "FAILED"

                                                        ? "text-red-400"

                                                        : txnType === "CREDIT"

                                                            ? "text-emerald-400"

                                                            : "text-cyan-400"

                                                }`}>

                                                    ₹ {
                                                        transaction.amount
                                                    }

                                                </td>

                                                {/* BALANCE */}

                                                <td className="p-5">

                                                    ₹ {

                                                        transaction.availableBalance
                                                        ||
                                                        0
                                                    }

                                                </td>

                                                {/* DATE */}

                                                <td className="p-5 text-slate-400">

                                                    {
                                                        transaction.createdAt
                                                    }

                                                </td>

                                            </tr>
                                        );
                                    })
                                }

                                </tbody>

                            </table>

                        </div>

                        {/* PAGINATION */}

                        <div className="flex justify-center items-center gap-4 mt-8">

                            <button

                                disabled={currentPage === 1}

                                onClick={() =>
                                    setCurrentPage(

                                        currentPage - 1
                                    )
                                }

                                className={`px-5 py-3 rounded-2xl ${

                                    currentPage === 1

                                        ? "bg-slate-800 opacity-50 cursor-not-allowed"

                                        : "bg-slate-700 hover:bg-slate-600"
                                }`}
                            >

                                Previous

                            </button>

                            <span className="text-lg font-semibold">

                                Page {
                                    currentPage
                                }
                                {" "}
                                of
                                {" "}
                                {
                                    totalPages
                                }

                            </span>

                            <button

                                disabled={
                                    currentPage === totalPages
                                }

                                onClick={() =>
                                    setCurrentPage(

                                        currentPage + 1
                                    )
                                }

                                className={`px-5 py-3 rounded-2xl ${

                                    currentPage === totalPages

                                        ? "bg-slate-800 opacity-50 cursor-not-allowed"

                                        : "bg-slate-700 hover:bg-slate-600"
                                }`}
                            >

                                Next

                            </button>

                        </div>

                    </>
                )
            }

        </div>
    );
}

export default Transactions;