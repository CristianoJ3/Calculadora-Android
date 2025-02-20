package com.example.calculadoraandroid


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadoraandroid.databinding.CalculadoraAndroidActivityBinding
import org.mariuszgromada.math.mxparser.Expression

class CalculadoraAndroid : AppCompatActivity() {

    private val binding by lazy {
        CalculadoraAndroidActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotoesCalculadora()
    }

    private fun configuraBotoesCalculadora(){

        val textoCalculadora = binding.textoCalculadora
        val resultadoFinal = binding.resultadoCalculo

        val botoesNumericos = listOf(
            binding.oneButton,
            binding.twoButton,
            binding.threeButton,
            binding.fourButton,
            binding.fiveButton,
            binding.sixButton,
            binding.sevenButton,
            binding.eightButton,
            binding.nineButton,
            binding.zeroButton
        )

        val botoesOperacoes = listOf(
            binding.plusButton,
            binding.minusButton,
            binding.timesButton,
            binding.divisionButton,
            binding.dotButton
        )

        val parenteses = listOf(
            binding.leftParentesesButton,
            binding.rightParentesesButton
        )

        binding.deleteButton.setOnClickListener{
            textoCalculadora.text = binding.textoCalculadora.text.dropLast(1)
        }

        // Botao de limpar
        binding.cleanButton.setOnClickListener{
            textoCalculadora.text = ""
            resultadoFinal.text = ""
        }

        // Botao para calcular
        binding.equalButton.setOnClickListener {
            val expressao = textoCalculadora.text.toString()

            try {
                val resultado = Expression(expressao).calculate()
                resultadoFinal.text = resultado.toString()
                textoCalculadora.text = ""
            } catch (e: Exception) {
                resultadoFinal.text = "Erro"
            }
        }

        // Adiciona número ao campo de texto
        botoesNumericos.forEach { botao ->
            botao.setOnClickListener {
               textoCalculadora.append(botao.text)
            }
        }

        // Adiciona operadores com validação
        botoesOperacoes.forEach { botao ->
            botao.setOnClickListener {
                val textoAtual = textoCalculadora.text.toString()

                if (textoAtual.isNotEmpty() && textoAtual.last().isDigit()) {
                    textoCalculadora.append(botao.text)
                }
            }
        }

        parenteses.forEach { botao ->
            botao.setOnClickListener {
                val textoAtual = textoCalculadora.text.toString()

                if (botao == binding.leftParentesesButton) {
                    // Permite adicionar "(" sempre
                    textoCalculadora.append(botao.text)
                } else if (botao == binding.rightParentesesButton) {
                    // Garante que só fecha parênteses se já houver um "(" antes
                    val qtdAbre = textoAtual.count { it == '(' }
                    val qtdFecha = textoAtual.count { it == ')' }

                    if (qtdAbre > qtdFecha && textoAtual.isNotEmpty() && textoAtual.last() != '(') {
                        textoCalculadora.append(botao.text)
                    }
                }
            }
        }

    }
}