// DESCRIPTION: Isabelle wrapper for OpenTheory

object Open_Theory extends isabelle.Isabelle_Tool.Body {

  import isabelle._

  lazy val options = Options.init()

  val session = "Open_Theory"
  val theory = "Open_Theory"

  val init_ml = """
    fun eval_in_theory thy s =
      ML_Context.eval_source_in (SOME (Proof_Context.init_global (Thy_Info.get_theory thy))) ML_Compiler.flags (Input.string s)
  """

  def apply(args: List[String]): Unit = {
    val results = Build.build(
      options = options,
      build_heap = true,
      sessions = List(session),
      progress = new Console_Progress()
    )

    if (!results.ok)
      error("Build failed")

    val args_ml = ML_Syntax.print_list(ML_Syntax.print_string_bytes)(args)
    val eval_ml = ML_Syntax.print_string_bytes(s"Open_Theory.main $args_ml")

    val opentheory_ml = s"""eval_in_theory "$session.$theory" $eval_ml"""

    println(opentheory_ml)

    val proc = ML_Process(
      options = options,
      logic = session,
      args = List("--eval", init_ml, "--eval", opentheory_ml)
    )

    proc.result(
      progress_stdout = println _,
      progress_stderr = Console.err.println _
    )
  }

}
