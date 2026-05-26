import { useState }
from "react";

import {
    useNavigate,
    Link
} from "react-router-dom";

import api from "../api/axios";

function Register() {

    const navigate = useNavigate();

    const [name, setName] =
        useState("");

    const [mobileNumber, setMobileNumber] =
        useState("");

    const [password, setPassword] =
        useState("");

    const [confirmPassword,
        setConfirmPassword] =
        useState("");

    const [message, setMessage] =
        useState("");

    const [error, setError] =
        useState("");

    // MOBILE VALIDATION
    const isValidMobile =
        (mobile) => {

            return /^[6-9]\d{9}$/
                .test(mobile);
        };

    const handleRegister =
        async (e) => {

            e.preventDefault();

            setError("");

            setMessage("");

            // EMPTY FIELD VALIDATION
            if (
                !name ||
                !mobileNumber ||
                !password ||
                !confirmPassword
            ) {

                setError(
                    "All fields are required"
                );

                return;
            }
        // NAME VALIDATION
        if (
            !/^[A-Za-z ]+$/.test(name)
        ) {

            setError(
                "Name should contain only alphabets"
            );

            return;
        }

            // MOBILE VALIDATION
            if (
                !isValidMobile(
                    mobileNumber
                )
            ) {

                setError(
                    "Enter valid 10 digit mobile number"
                );

                return;
            }

            // PASSWORD LENGTH
            if (
                password.length < 5
            ) {

                setError(
                    "Password must contain minimum 5 characters"
                );

                return;
            }

            // CONFIRM PASSWORD
            if (
                password !==
                confirmPassword
            ) {

                setError(
                    "Passwords do not match"
                );

                return;
            }

            try {

                const response =
                    await api.post(
                        "/auth/register",
                        {
                            name,
                            mobileNumber,
                            password
                        }
                    );

                setMessage(
                    response.data
                );

                setTimeout(() => {

                    navigate("/login");

                }, 2000);

            } catch (err) {

                setError(

                    err.response?.data?.message ||

                    err.response?.data?.error ||

                    "Registration failed"
                );
            }
        };
    const [showPassword,
        setShowPassword] =
        useState(false);

    return (

        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-950 via-slate-900 to-emerald-950">

            <div className="w-full max-w-md">

                <div className="bg-slate-900/70 backdrop-blur-lg border border-slate-700 rounded-3xl shadow-2xl p-10">

                    <div className="text-center">

                        <h1 className="text-4xl font-bold text-white">

                            Create Account

                        </h1>

                        <p className="text-slate-400 mt-3">

                            Join Mini Wallet

                        </p>

                    </div>

                    <form
                        onSubmit={handleRegister}
                        className="mt-8"
                    >

                        {/* NAME */}

                        <div className="mb-5">

                            <label className="text-slate-300 block mb-2">

                                Name

                            </label>

                            <input
                                type="text"
                                placeholder="Enter your name"
                                value={name}
                                onChange={(e) =>
                                    setName(
                                        e.target.value
                                    )
                                }
                                className="w-full p-4 rounded-xl bg-slate-800 border border-slate-700 text-white"
                            />

                        </div>

                        {/* MOBILE */}

                        <div className="mb-5">

                            <label className="text-slate-300 block mb-2">

                                Mobile Number

                            </label>

                           <input
                               type="text"

                               placeholder="Enter mobile number"

                               value={mobileNumber}

                               onChange={(e) => {

                                   // ALLOW ONLY NUMBERS

                                   const value =

                                       e.target.value
                                           .replace(/\D/g, "")

                                           // LIMIT TO 10 DIGITS
                                           .slice(0, 10);

                                   setMobileNumber(value);
                               }}

                               maxLength={10}

                               className="w-full p-4 rounded-xl bg-slate-800 border border-slate-700 text-white"
                           />

                        </div>

                       {/* PASSWORD */}

                       <div className="mb-5">

                           <label className="text-slate-300 block mb-2">

                               Password

                           </label>

                           <div className="relative">

                               <input

                                   type={
                                       showPassword
                                           ? "text"
                                           : "password"
                                   }

                                   placeholder="Enter password"

                                   value={password}

                                   onChange={(e) =>
                                       setPassword(
                                           e.target.value
                                       )
                                   }

                                   className="w-full p-4 pr-14 rounded-xl bg-slate-800 border border-slate-700 text-white placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-emerald-500"
                               />

                               <button

                                   type="button"

                                   onClick={() =>
                                       setShowPassword(
                                           !showPassword
                                       )
                                   }

                                   className="absolute right-4 top-1/2 -translate-y-1/2 text-white text-lg"
                               >

                                   {
                                       showPassword
                                           ? "🔒"
                                           : "👁️"
                                   }

                               </button>

                           </div>

                       </div>

                        {/* CONFIRM PASSWORD */}

                        <div className="mb-5">

                            <label className="text-slate-300 block mb-2">

                                Confirm Password

                            </label>

                            <div className="relative">

                                <input

                                    type={
                                        showPassword
                                            ? "text"
                                            : "password"
                                    }

                                    placeholder="Confirm password"

                                    value={confirmPassword}

                                    onChange={(e) =>
                                        setConfirmPassword(
                                            e.target.value
                                        )
                                    }

                                    className="w-full p-4 pr-14 rounded-xl bg-slate-800 border border-slate-700 text-white placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-emerald-500"
                                />

                                <button

                                    type="button"

                                    onClick={() =>
                                        setShowPassword(
                                            !showPassword
                                        )
                                    }

                                    className="absolute right-4 top-1/2 -translate-y-1/2 text-white text-lg"
                                >

                                    {
                                        showPassword
                                            ? "🔒"
                                            : "👁️"
                                    }

                                </button>

                            </div>

                        </div>

                        {/* SUCCESS */}

                        {message && (

                            <div className="bg-emerald-500/20 text-emerald-400 p-3 rounded-xl mb-5">

                                {message}

                            </div>
                        )}

                        {/* ERROR */}

                        {error && (

                            <div className="bg-red-500/20 text-red-400 p-3 rounded-xl mb-5">

                                {error}

                            </div>
                        )}

                        <button
                            type="submit"
                            className="w-full bg-emerald-500 hover:bg-emerald-600 transition-all duration-300 text-white p-4 rounded-xl font-semibold shadow-lg"
                        >

                            Register

                        </button>

                    </form>

                    <div className="mt-6 text-center text-slate-400">

                        Already have account?

                        <Link
                            to="/login"
                            className="text-emerald-400 ml-2 hover:text-emerald-300"
                        >

                            Login

                        </Link>

                    </div>

                </div>

            </div>

        </div>
    );
}

export default Register;