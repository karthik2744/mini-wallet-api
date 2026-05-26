import {
    useEffect,
    useState
} from "react";

import {
    useNavigate
} from "react-router-dom";

import api from "../api/axios";

function AdminTransactions() {

    const navigate = useNavigate();

    const exportCSV = async () => {

        try {

            // FETCH ALL RECORDS

            const response =
                await api.get(

                    "/api/admin/transactions/filter",

                    {
                        params: {

                            msisdn: search,

                            type:
                                typeFilter || null,

                            status:
                                statusFilter || null,

                            page: 0,

                            size: 100000
                        }
                    }
                );

            const allTransactions =
                response.data.content;

            const headers = [

                "Reference ID",

                "Mobile",

                "Type",

                "Status",

                "Amount",

                "Balance",

                "Date"
            ];

            const rows = allTransactions.map(
                (t) => [

                    t.referenceId,

                    t.mobileNumber
                    ||
                    t.msisdn,

                    t.type
                    ||
                    t.transactionType,

                    t.status,

                    t.amount,

                    t.availableBalance,

                    new Date(
                        t.createdAt
                    ).toLocaleDateString(
                        "en-GB",
                        {
                            day: "2-digit",
                            month: "long",
                            year: "numeric"
                        }
                    )
                ]
            );

            let csvContent =

                headers.join(",") + "\n";

            rows.forEach((row) => {

                csvContent +=
                    row.join(",") + "\n";
            });

            const blob =
                new Blob(

                    [csvContent],

                    {
                        type:
                            "text/csv;charset=utf-8;"
                    }
                );

            const url =
                window.URL.createObjectURL(blob);

            const link =
                document.createElement("a");

            link.href = url;

            link.setAttribute(

                "download",

                "all_transactions.csv"
            );

            document.body.appendChild(link);

            link.click();

            document.body.removeChild(link);

        } catch (err) {

            console.log(err);
        }
    };

    const [transactions,
        setTransactions] =
        useState([]);

    const [page, setPage] =
        useState(0);

    const [totalPages,
        setTotalPages] =
        useState(0);

    const [search,
        setSearch] =
        useState("");

    const [typeFilter,
        setTypeFilter] =
        useState("");

    const [statusFilter,
        setStatusFilter] =
        useState("");

    const fetchTransactions =
        async () => {

            try {

                const response =
                    await api.get(

                        "/api/admin/transactions/filter",

                        {
                            params: {

                                msisdn: search,

                                type:
                                    typeFilter || null,

                                status:
                                    statusFilter || null,

                                page,

                                size: 10
                            }
                        }
                    );

                setTransactions(
                    response.data.content
                );

                setTotalPages(
                    response.data.totalPages
                );

            } catch (err) {

                console.log(err);
            }
        };

    useEffect(() => {

        fetchTransactions();

    }, [
        page,
        typeFilter,
        statusFilter
    ]);

    const logout = () => {

        const confirmed =

            window.confirm(

                "Are you sure you want to logout?"
            );

        if (!confirmed) {

            return;
        }

        localStorage.clear();

        navigate("/login");
    };

    return (

        <div className="flex min-h-screen bg-slate-950 text-white">

            {/* SIDEBAR */}

            <div className="w-72 bg-slate-900 border-r border-slate-800 p-6 flex flex-col">

                <h1 className="text-3xl font-bold mb-12">

                    Mini Wallet

                </h1>

                <div className="space-y-4">

                    <button
                        onClick={() =>
                            navigate("/admin-dashboard")
                        }
                        className="w-full bg-slate-800 hover:bg-slate-700 p-4 rounded-2xl text-left"
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
                        className="w-full bg-emerald-500 text-white p-4 rounded-2xl text-left"
                    >

                        Transactions

                    </button>

                </div>

                <div className="mt-auto">

                    <button
                        onClick={logout}
                        className="w-full bg-red-500 hover:bg-red-600 p-4 rounded-2xl"
                    >

                        Logout

                    </button>

                </div>

            </div>

            {/* MAIN */}

            <div className="flex-1 p-10">

                <div className="flex items-center justify-between mb-8">

                    <h1 className="text-4xl font-bold">

                        Transactions

                    </h1>

                </div>

                {/* FILTERS */}

                <div className="flex gap-4 mb-8">

                    <input
                        type="text"
                        placeholder="Search mobile number"
                        value={search}
                        onChange={(e) =>
                            setSearch(
                                e.target.value
                            )
                        }
                        className="bg-slate-900 border border-slate-700 p-4 rounded-2xl w-80"
                    />

                    <button
                        onClick={() => {

                            setPage(0);

                            fetchTransactions();
                        }}
                        className="bg-emerald-500 hover:bg-emerald-600 px-6 rounded-2xl"
                    >

                        Search

                    </button>

                    <select
                        value={typeFilter}
                        onChange={(e) => {

                            setPage(0);

                            setTypeFilter(
                                e.target.value
                            );
                        }}
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

                    <select
                        value={statusFilter}
                        onChange={(e) => {

                            setPage(0);

                            setStatusFilter(
                                e.target.value
                            );
                        }}
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

                {/* TABLE */}

                <div className="bg-slate-900 rounded-3xl overflow-hidden border border-slate-800">

                    <table className="w-full">

                        <thead className="bg-slate-800">

                        <tr>

                            <th className="text-left p-5">
                                Reference ID
                            </th>

                            <th className="text-left p-5">
                                Mobile
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

                        {transactions.map((transaction) => {

                            const txnType =

                                transaction.type
                                ||
                                transaction.transactionType
                                ||
                                transaction.transaction_type;

                            return (

                                <tr
                                    key={transaction.referenceId}
                                    className="border-t border-slate-800"
                                >

                                    {/* REF ID */}

                                    <td className="p-5">

                                        {transaction.referenceId}

                                    </td>

                                    {/* MOBILE */}

                                    <td className="p-5">

                                        {
                                            transaction.mobileNumber
                                            ||
                                            transaction.msisdn
                                        }

                                    </td>

                                    {/* TYPE */}

                                    <td className="p-5">

                                        <span className={`px-4 py-2 rounded-full text-sm font-semibold ${

                                            txnType === "CREDIT"

                                                ? "bg-emerald-500/20 text-emerald-400"

                                                : "bg-cyan-500/20 text-cyan-400"

                                        }`}>

                                            {txnType}

                                        </span>

                                    </td>

                                    {/* STATUS */}

                                    <td className="p-5">

                                        <span className={`px-4 py-2 rounded-full text-sm font-semibold ${

                                            transaction.status === "SUCCESS"

                                                ? "bg-emerald-500/20 text-emerald-400"

                                                : "bg-red-500/20 text-red-400"

                                        }`}>

                                            {transaction.status}

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

                                        ₹ {transaction.amount}

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
                                            new Date(
                                                transaction.createdAt
                                            ).toLocaleDateString(
                                                "en-GB",
                                                {
                                                    day: "2-digit",
                                                    month: "long",
                                                    year: "numeric"
                                                }
                                            )
                                        }

                                    </td>

                                </tr>
                            );
                        })}

                        </tbody>

                    </table>

                </div>

                {/* PAGINATION */}

                <div className="flex items-center justify-center gap-4 mt-8">

                    <button
                        disabled={page === 0}
                        onClick={() =>
                            setPage(page - 1)
                        }
                        className="bg-slate-800 px-5 py-3 rounded-xl disabled:opacity-40"
                    >

                        Previous

                    </button>

                    <p>

                        Page {page + 1}
                        {" "}
                        of
                        {" "}
                        {totalPages}

                    </p>

                    <button
                        onClick={exportCSV}
                        className="bg-cyan-500 hover:bg-cyan-600 px-6 rounded-2xl"
                    >

                        Export CSV

                    </button>

                    <button
                        disabled={
                            page + 1 >= totalPages
                        }
                        onClick={() =>
                            setPage(page + 1)
                        }
                        className="bg-slate-800 px-5 py-3 rounded-xl disabled:opacity-40"
                    >

                        Next

                    </button>

                </div>

            </div>

        </div>
    );
}

export default AdminTransactions;